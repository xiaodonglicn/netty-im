package test.business;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import test.business.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 存储在线连接
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 存储用户名与连接的映射
    private static final ConcurrentMap<String, String> userChannelMap = new ConcurrentHashMap<>();

    private final UserService userService;

    public TextWebSocketFrameHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
        ctx.writeAndFlush(new TextWebSocketFrame("请输入用户名:"));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());

        String username = userChannelMap.get(ctx.channel().id().asLongText());
        if (username != null) {
            userChannelMap.remove(ctx.channel().id().asLongText());
            userService.logout(username);
            Message leaveMessage = new Message(Message.MessageType.LEAVE, username, null, null);
            channels.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(leaveMessage)));
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String content = msg.text();
        String channelId = ctx.channel().id().asLongText();

        String username = userChannelMap.get(channelId);
        String timestamp = new SimpleDateFormat("HH:mm").format(new Date());
        if (username == null) {
            if (userService.isUsernameExists(content)) {
                ctx.writeAndFlush(new TextWebSocketFrame("用户名已存在，请重新输入:"));
                return;
            }

            userChannelMap.put(channelId, content);
            Message joinMessage = new Message(Message.MessageType.JOIN, content, null, timestamp);
            channels.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(joinMessage)));
        } else {
            try {
                // ✅ 解析前端发送的 JSON 消息
                JsonObject jsonObject = new Gson().fromJson(content, JsonObject.class);
                if (jsonObject.has("type") && "CHAT".equals(jsonObject.get("type").getAsString())) {
                    String chatContent = jsonObject.get("content").getAsString();
                    Message chatMessage = new Message(Message.MessageType.CHAT, username, chatContent, timestamp);
                    channels.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(chatMessage)));
                }
            } catch (Exception e) {
                // 如果不是 JSON 格式，当作普通文本处理
                Message chatMessage = new Message(Message.MessageType.CHAT, username, content, timestamp);
                channels.writeAndFlush(new TextWebSocketFrame(new Gson().toJson(chatMessage)));
            }
        }
    }

}
