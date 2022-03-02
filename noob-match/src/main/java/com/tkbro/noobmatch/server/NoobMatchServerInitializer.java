package com.tkbro.noobmatch.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Component
public class NoobMatchServerInitializer {
    private static final Logger logger = LoggerFactory.getLogger(NoobMatchServerInitializer.class);

    private final ServerChannelInitializer serverChannelInitializer;

    @Value("${boss.thread.count}")
    private int bossCount;
    @Value("${worker.thread.count}")
    private int workerCount;
    @Value("${tcp.port}")
    private int port;

    public NoobMatchServerInitializer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }

    @PostConstruct
    public void start() {

        logger.info("Server Bootstrap init start.");

        EventLoopGroup bossGroup = new NioEventLoopGroup(bossCount);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerCount);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.serverChannelInitializer);

            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(port)).sync();
            logger.info("Server Bootstrap bind finish.");

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

            logger.info("Server close.");
        }
    }
}
