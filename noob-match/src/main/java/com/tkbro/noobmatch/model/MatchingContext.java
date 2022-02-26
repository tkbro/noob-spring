package com.tkbro.noobmatch.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchingContext {
    private String userId;
    private long createdTime;
    private short tryCount;
    private long matchedTime;
}
