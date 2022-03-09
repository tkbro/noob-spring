package com.tkbro.noobmatch.protocol.codec;

import com.tkbro.noobmatch.protocol.MatchProtocol;
import com.tkbro.noobmatch.protocol.MatchProtocolType;
import com.tkbro.noobmatch.protocol.MatchProtocolTypeResolver;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MatchProtocolCodec extends ByteToMessageCodec<MatchProtocol> {

    final private MatchProtocolTypeResolver matchProtocolTypeResolver;

    public MatchProtocolCodec(MatchProtocolTypeResolver matchProtocolTypeResolver) {
        this.matchProtocolTypeResolver = matchProtocolTypeResolver;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MatchProtocol matchProtocol, ByteBuf byteBuf) {
        matchProtocol.encode(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        byte protocolId = byteBuf.readByte();
        MatchProtocolType protocolType = this.matchProtocolTypeResolver.getMatchProtocolType(protocolId).orElseThrow();

        int dataSize = byteBuf.readInt();
        ByteBuf data = byteBuf.readBytes(dataSize);
        MatchProtocol protocol = new MatchProtocol();
        protocol.setProtocolType(protocolType);
        protocol.setDataSize(byteBuf.readableBytes());
        protocol.setData(data);

        log.debug("{} Received payload data is...\n{}", channelHandlerContext.channel(), data);

        list.add(protocol);
    }
}
