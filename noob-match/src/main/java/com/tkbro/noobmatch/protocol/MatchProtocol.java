package com.tkbro.noobmatch.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.IllegalReferenceCountException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class MatchProtocol implements ByteBufHolder {
    private BaseProtocolType protocolType;
    private int dataSize;
    private ByteBuf data;

    @Override
    public ByteBuf content() {
        if (this.data.refCnt() <= 0) {
            throw new IllegalReferenceCountException();
        } else {
            return this.data;
        }
    }

    @Override
    public ByteBufHolder copy() {
        return this.replace(this.data.copy());
    }

    @Override
    public ByteBufHolder duplicate() {
        return this.replace(this.data.duplicate());
    }

    @Override
    public ByteBufHolder retainedDuplicate() {
        return this.replace(this.data.retainedDuplicate());
    }

    @Override
    public ByteBufHolder replace(ByteBuf byteBuf) {
        MatchProtocol protocol = new MatchProtocol();
        protocol.setProtocolType(protocolType);
        protocol.setDataSize(byteBuf.readableBytes());
        protocol.setData(byteBuf);
        return protocol;
    }

    @Override
    public int refCnt() {
        return this.data.refCnt();
    }

    @Override
    public ByteBufHolder retain() {
        this.data.retain();
        return this;
    }

    @Override
    public ByteBufHolder retain(int i) {
        this.data.retain(i);
        return this;
    }

    @Override
    public ByteBufHolder touch() {
        this.data.touch();
        return this;
    }

    @Override
    public ByteBufHolder touch(Object o) {
        this.data.touch(o);
        return this;
    }

    @Override
    public boolean release() {
        return this.data.release();
    }

    @Override
    public boolean release(int i) {
        return this.data.release(i);
    }

    public void encode(ByteBuf encoded) {
        try {
            encoded.writeByte(protocolType.getProtocolId());

            boolean hasData = this.data != null && this.data.isReadable();
            if (hasData) {
                encoded.writeInt(this.data.readableBytes());
                encoded.writeBytes(this.data);
            } else {
                encoded.writeInt(0);
            }

        } catch (Exception e) {
            log.error(e.toString());
            throw e;
        }
    }
}
