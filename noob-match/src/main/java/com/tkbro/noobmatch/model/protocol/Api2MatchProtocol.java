package com.tkbro.noobmatch.model.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.util.IllegalReferenceCountException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// todo
// ProtocolType 을 추가 (Client 와 동기화 고려 필요)
// ProtocolType 에 1:1 매칭되는 메서드를 연결해주는 방법이 필요하

@Builder
@Getter
public class Api2MatchProtocol implements ByteBufHolder {

    private ByteBuf data;

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
        return Api2MatchProtocol.builder().data(byteBuf).build();
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
}
