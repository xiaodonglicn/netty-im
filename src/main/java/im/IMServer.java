package im;

import handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Yeast
 * @Date: 2024/02/28 10:17 AM
 */
public class IMServer {

    public static final Map<String, Channel> USERS = new ConcurrentHashMap<>();
    public static final Map<String, ChannelGroup> GROUP_MAP = new ConcurrentHashMap<>();
//    public static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void  start() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        // 添加http编码解码器
                        pipeline.addLast(new HttpServerCodec())
                                // 支持大数据
                                .addLast(new ChunkedWriteHandler())
                                // 消息聚合
                                .addLast(new HttpObjectAggregator(1024 * 64))
                                // websocket
                                .addLast(new WebSocketServerProtocolHandler("/"))
                                .addLast(new WebSocketHandler());
                    }
                });

        try {
            bootstrap.bind(8080).sync();
        } catch (InterruptedException e) {
        }
    }

}
