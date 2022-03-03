package com.tkbro.noobtestclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;
import java.util.Scanner;

@SpringBootApplication
public class NoobTestClientApplication {
    private static String host = "localhost";

    @Value("port:8088")
    private static int port;

    public static void main(String[] args) {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("client"));
        try {
            // SpringApplication.run(NoobTestClientApplication.class, args);

            Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup);

            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(new InetSocketAddress(host, port));
            bootstrap.handler(new ClientInitializer());

            Channel serverChannel = bootstrap.connect().sync().channel();

            Scanner scanner = new Scanner(System.in);

            String input;
            ChannelFuture future;

            while (true) {
                input = scanner.nextLine();
                future = serverChannel.writeAndFlush(input.concat("\n"));

//                if ("test".equals(input)) {
//
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    ObjectOutputStream oos = new ObjectOutputStream(bos);
//                    oos.writeBytes(input);
//                    byte[] bs = bos.toByteArray();
//
//                    ByteBuf buf = Unpooled.directBuffer();
//                    buf.writeBytes(bs);
//
//                    Api2MatchProtocol test = Api2MatchProtocol.builder().data(buf).build();
//                    future = serverChannel.writeAndFlush(test.content());
//                } else {
//                    future = serverChannel.writeAndFlush(input.concat("\n"));
//                }

                if ("quit".equals(input))
                {
                    serverChannel.closeFuture().sync();
                    break;
                }

                if (future != null)
                {
                    future.sync();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }


    }

}
