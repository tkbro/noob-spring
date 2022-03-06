package com.tkbro.noobmatch.protocol;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum MatchProtocolType implements BaseProtocolType {
    TEST1((byte)1),
    TEST2((byte)2),
    TEST3((byte)3);

    private static Map<Byte, MatchProtocolType> protocolMap = new HashMap();

    private final byte protocolId;

    @Override
    public byte getProtocolId() {
        return protocolId;
    }

    private MatchProtocolType(byte protocolId) {
        this.protocolId = protocolId;
    }

    static {
        Arrays.stream(values()).forEach((v) -> {
            protocolMap.put(v.getProtocolId(), v);
        });
    }
}
