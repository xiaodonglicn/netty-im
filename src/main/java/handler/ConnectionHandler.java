package handler;

import com.alibaba.fastjson2.JSON;
import im.Command;
import im.IMServer;
import im.Result;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 11:14 AM
 */
public class ConnectionHandler  {

   public static void execute(ChannelHandlerContext channelHandlerContext, Command command) {
      if (IMServer.USERS.containsKey(command.getNickname())) {
         channelHandlerContext.channel().writeAndFlush(Result.success(command.getNickname() + "已在线" +"_"+ JSON.toJSONString(IMServer.USERS.keySet())));
         channelHandlerContext.channel().disconnect();
         return;
      }
      IMServer.USERS.put(command.getNickname(), channelHandlerContext.channel());
      channelHandlerContext.channel().writeAndFlush(Result.success("连接成功" +"_"+ JSON.toJSONString(IMServer.USERS.keySet())));
   }
}
