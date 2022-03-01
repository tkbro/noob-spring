package com.tkbro.noobmatch.model.codec;

import com.tkbro.noobmatch.model.protocol.Api2MatchProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

// todo : 추상화 필요, api2Match 타입 뿐아니라 그냥 게임 공통 메시지 타입으로 변경 필요 (ex NoobGameMessage)
@Slf4j
public class Api2MatchProtocolCodec extends ByteToMessageCodec<Api2MatchProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Api2MatchProtocol api2MatchProtocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(api2MatchProtocol.getData());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readableBytes = byteBuf.readableBytes();
        ByteBuf data = byteBuf.readBytes(readableBytes);
        Api2MatchProtocol api2MatchProtocol = Api2MatchProtocol.builder().data(data).build();

        log.debug("{} Received payload data is...\n{}", channelHandlerContext.channel(), data);

        list.add(api2MatchProtocol);
    }
}
