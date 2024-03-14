package handler;

import com.alibaba.fastjson2.JSON;
import im.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 11:14 AM
 */
public class ChatHandler {

   public static void execute(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame frame) {
      String text = frame.text();
      ChatMessage chatMessage = JSON.parseObject(text, ChatMessage.class);
      switch (MessageType.match(chatMessage.getMessageType())) {
         case PRIVATE -> {
            if (StringUtil.isNullOrEmpty(chatMessage.getTarget())) {
               channelHandlerContext.channel().writeAndFlush(Result.fail("请指定目标对象"));
               return;
            }
            Channel channel = IMServer.USERS.get(chatMessage.getTarget());
            if (channel == null || !channel.isActive()) {
               // 不在线
               channelHandlerContext.channel().writeAndFlush(Result.fail("对方不在线"));
            } else {
               // 在线
               channel.writeAndFlush(Result.success("私聊消息：" + chatMessage.getContent(), chatMessage.getTarget()));
            }
         }
         case GROUP -> {
            if (StringUtil.isNullOrEmpty(chatMessage.getTarget())) {
               channelHandlerContext.channel().writeAndFlush(Result.fail("请指定目标对象"));
               return;
            }
            ChannelGroup channelGroup = IMServer.GROUP_MAP.get(chatMessage.getTarget());
            if (channelGroup == null) {
               channelHandlerContext.channel().writeAndFlush(Result.fail("群聊不存在"));
               return;
            }
            channelGroup.writeAndFlush(Result.success("群聊消息：" + chatMessage.getContent(), chatMessage.getTarget()));
         }
         default -> channelHandlerContext.channel().writeAndFlush(Result.fail("不支持的消息类型"));
      }
   }
}
