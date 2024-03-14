package handler;

import com.alibaba.fastjson2.JSON;
import im.Command;
import im.CommandType;
import im.Result;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 11:14 AM
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame)  {
        System.out.println(textWebSocketFrame.text());
        try {
            Command command = JSON.parseObject(textWebSocketFrame.text(), Command.class);
            switch (CommandType.match(command.getCode())){
                // 执行连接
                case CONNECTION -> ConnectionHandler.execute(channelHandlerContext, command);
                case CHAT -> ChatHandler.execute(channelHandlerContext, textWebSocketFrame);
                case JOIN_GROUP -> GroupHandler.executeJoin(channelHandlerContext, command);
                case CREATE_GROUP -> GroupHandler.executeCreate(channelHandlerContext, command);
                default -> channelHandlerContext.channel().writeAndFlush(Result.fail("编码不能识别"));
            }
        } catch (Exception e) {
            channelHandlerContext.channel().writeAndFlush(Result.fail(e.getMessage()));
        }
    }
}
