package com.tkbro.noobmatch.service;

import com.tkbro.noobmatch.model.MatchingJoinContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.metrics.ApplicationStartup;

class MatchServiceTest {

    MatchService matchService = new MatchService();

    @Test
    public void joinMatching() throws InterruptedException {
        MatchingJoinContext joinContext1 = MatchingJoinContext.builder().userId("test1").createdTime(1).build();
        MatchingJoinContext joinContext2 = MatchingJoinContext.builder().userId("test2").createdTime(2).build();

        matchService.joinMatching(joinContext1);
        assert matchService.getPoolSize() == 1;
        Thread.sleep(3000L);
        matchService.joinMatching(joinContext2);
        assert matchService.getPoolSize() == 2;
    }
}