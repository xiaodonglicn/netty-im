package handler;

import im.Command;
import im.IMServer;
import im.Result;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 11:14 AM
 */
public class GroupHandler {

   public static void executeCreate(ChannelHandlerContext channelHandlerContext, Command command) {
      Channel channel = channelHandlerContext.channel();
      DefaultChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
      IMServer.GROUP_MAP.put(command.getNickname(), channelGroup);
      channelGroup.add(channel);
      channel.writeAndFlush(Result.success(command.getNickname() + "创建群聊成功"));
   }

   public static void executeJoin(ChannelHandlerContext channelHandlerContext, Command command) {
      Channel channel = channelHandlerContext.channel();
      if (!IMServer.GROUP_MAP.containsKey(command.getNickname())) {
         channel.writeAndFlush(Result.fail("群聊不存在"));
         return;
      }
      ChannelGroup channelGroup = IMServer.GROUP_MAP.get(command.getNickname());
      channelGroup.add(channel);
      channel.writeAndFlush(Result.success(command.getNickname() + "加入群聊成功"));
   }
}
