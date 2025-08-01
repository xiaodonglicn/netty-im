package test.business;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;
import test.business.service.UserService;

import javax.annotation.Resource;

@Component
public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private UserService userService;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // HTTP编解码
        pipeline.addLast(new HttpServerCodec());
        // 支持大文件传输
        pipeline.addLast(new ChunkedWriteHandler());
        // 聚合HTTP消息
        pipeline.addLast(new HttpObjectAggregator(65536));

        // WebSocket协议处理
        pipeline.addLast(new WebSocketServerProtocolHandler("/chat"));

        // ✅ 每次都创建一个新的 handler 实例
        pipeline.addLast(new TextWebSocketFrameHandler(userService));
    }
}
