package com.tkbro.noobmatch.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchingJoinContext {
    private String userId;
    private long createdTime;
}
