package com.tkbro.noobtestclient;

import com.tkbro.noobmatch.model.protocol.Api2MatchProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;

@SpringBootApplication
public class NoobTestClientApplication {
    private static String host = "localhost";
    private static int port = 8088;

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

                if ("test".equals(input)) {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeBytes(input);
                    byte[] bs = bos.toByteArray();

                    ByteBuf buf = Unpooled.directBuffer();
                    buf.writeBytes(bs);

                    Api2MatchProtocol test = Api2MatchProtocol.builder().data(buf).build();
                    future = serverChannel.writeAndFlush(test.content());
                } else {
                    future = serverChannel.writeAndFlush(input.concat("\n"));
                }

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
