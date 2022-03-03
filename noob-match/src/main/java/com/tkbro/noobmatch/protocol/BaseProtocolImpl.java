package com.tkbro.noobmatch.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.IllegalReferenceCountException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Builder
@Getter
@Setter
public class BaseProtocolImpl implements ByteBufHolder, BaseProtocol {

    private ByteBuf data;
    private ProtocolType protocolType;

    @Override
    public ByteBuf content() {
        if (this.data.refCnt() <= 0) {
            throw new IllegalReferenceCountException(this.data.refCnt());
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
        return new BaseProtocolImpl(byteBuf, protocolType);
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

    @Override
    public ProtocolType getType() {
        return this.protocolType;
    }
}
