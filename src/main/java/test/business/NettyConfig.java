package test.business;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyConfig {

    @Value("${netty.port}")
    private int port;

    @Bean
    public EventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public EventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public ServerBootstrap serverBootstrap(ChatServerInitializer chatServerInitializer) {
        return new ServerBootstrap()
                .group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(chatServerInitializer);
    }

    @Bean
    public Channel channel(ServerBootstrap serverBootstrap) throws InterruptedException {
        Channel channel = serverBootstrap.bind(port).sync().channel();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            channel.close();
            bossGroup().shutdownGracefully();
            workerGroup().shutdownGracefully();
        }));
        return channel;
    }
}
