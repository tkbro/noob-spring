package com.tkbro.noobmatch.protocol.codec;

import com.tkbro.noobmatch.protocol.BaseProtocolImpl;
import com.tkbro.noobmatch.protocol.ProtocolType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

// todo : 추상화 필요, api2Match 타입 뿐아니라 그냥 게임 공통 메시지 타입으로 변경 필요 (ex NoobGameMessage)
@Slf4j
public class BaseProtocolCodec extends ByteToMessageCodec<BaseProtocolImpl> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, BaseProtocolImpl baseProtocol, ByteBuf byteBuf) {
        byteBuf.writeBytes(baseProtocol.getData());
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        int readableBytes = byteBuf.readableBytes();
        ByteBuf data = byteBuf.readBytes(readableBytes);

        // todo: bytes 읽어서 type 구분 해야함
        BaseProtocolImpl protocol = BaseProtocolImpl.builder()
                .data(data)
                .protocolType(ProtocolType.TEST1)
                .build();

        log.debug("{} Received payload data is...\n{}", channelHandlerContext.channel(), data);

        list.add(protocol);
    }
}
