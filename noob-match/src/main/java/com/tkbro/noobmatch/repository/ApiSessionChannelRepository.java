package com.tkbro.noobmatch.repository;

import io.netty.channel.Channel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class ApiSessionChannelRepository {
    private HashMap<String, Channel> apiSessionChannelMap = new HashMap<>();

    public void setChannel(Channel channel) {
        this.apiSessionChannelMap.put(channel.id().asLongText(), channel);
    }

    public boolean removeChannel(Channel channel) {
        return this.apiSessionChannelMap.remove(channel.id().asLongText()) != null;
    }

    public Channel getChannel(String key) {
        return apiSessionChannelMap.get(key);
    }
}
