package com.tkbro.noobtestclient;

import com.tkbro.noobmatch.protocol.MatchProtocol;
import com.tkbro.noobmatch.protocol.MatchProtocolType;
import com.tkbro.noobmatch.protocol.MatchProtocolTypeResolver;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
public class NoobTestClientApplication {
    private static String host = "localhost";

    // @Value("port:8088") // todo: 안먹히는데 수정 필
    private static int port = 8088;

    public static void main(String[] args) {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("client"));
        try {
            // SpringApplication.run(NoobTestClientApplication.class, args);

            Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup);

            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(new InetSocketAddress(host, port));
            MatchProtocolTypeResolver resolver = new MatchProtocolTypeResolver(new ArrayList<>());
            resolver.afterPropertiesSet();
            bootstrap.handler(new ClientInitializer(resolver));

            Channel serverChannel = bootstrap.connect().sync().channel();

            Scanner scanner = new Scanner(System.in);

            String input;
            ChannelFuture future;
            MatchProtocolType type = null;

            while (true) {
                input = scanner.nextLine();

                switch (input) {
                    case "1": {
                        type = MatchProtocolType.TEST1;
                        break;
                    }
                    case "2": {
                        type = MatchProtocolType.TEST2;
                        break;
                    }
                    case "3": {
                        type = MatchProtocolType.TEST3;
                        break;
                    }
                }

                if (type != null) {
                    MatchProtocol message = generateProtocol(type, input);
                    future = serverChannel.writeAndFlush(message);
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

    private static MatchProtocol generateProtocol(MatchProtocolType type, String input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeBytes(input);

        byte[] bs = bos.toByteArray();
        ByteBuf buf = Unpooled.directBuffer();
        buf.writeBytes(bs);

        MatchProtocol protocol = new MatchProtocol();
        protocol.setProtocolType(type);
        protocol.setDataSize(buf.readableBytes());
        protocol.setData(buf);
        return protocol;
    }

}
