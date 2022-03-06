package com.tkbro.noobtestclient;

import com.tkbro.noobmatch.protocol.MatchProtocolTypeResolver;
import com.tkbro.noobmatch.protocol.codec.MatchProtocolCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {

    private final MatchProtocolTypeResolver matchProtocolTypeResolver;

    public ClientInitializer(MatchProtocolTypeResolver matchProtocolTypeResolver) {
        this.matchProtocolTypeResolver = matchProtocolTypeResolver;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        //pipeline.addLast(new LineBasedFrameDecoder(65536));
        //pipeline.addLast(new StringDecoder());
        //pipeline.addLast(new StringEncoder());
        pipeline.addLast(new MatchProtocolCodec(this.matchProtocolTypeResolver));
        pipeline.addLast(new ClientHandler());
    }
}
