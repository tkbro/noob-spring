package com.tkbro.noobmatch.controller;

import com.tkbro.noobmatch.annotation.MatchProtocolController;
import com.tkbro.noobmatch.annotation.MatchProtocolMapper;
import com.tkbro.noobmatch.model.MatchingJoinContext;
import com.tkbro.noobmatch.protocol.MatchProtocol;
import com.tkbro.noobmatch.protocol.MatchProtocolType;
import com.tkbro.noobmatch.service.MatchService;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@MatchProtocolController
public class BaseProtocolController {

    private MatchService matchService;

    @Autowired
    BaseProtocolController(MatchService matchService) {
        this.matchService = matchService;
    }

    @MatchProtocolMapper(protocolType = MatchProtocolType.TEST1)
    public void Handle(MatchProtocol protocol) {
        // mock
        MatchingJoinContext joinContext1 = MatchingJoinContext.builder().userId("test1").createdTime(1).build();

        matchService.joinMatching(joinContext1);
    }
}
