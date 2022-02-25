package com.tkbro.noobmatch;

import com.tkbro.noobmatch.server.ServerChannelInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class NoobMatchApplicationContextLoaderTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assert applicationContext.getBean(ServerChannelInitializer.class) != null;
    }
}