package com.tkbro.noobmatch.protocol;

import com.tkbro.noobmatch.controller.ProtocolController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@Slf4j
public class ProtocolTypeResolver implements InitializingBean {

    private final List<ProtocolController> controllers ;

    @Autowired
    public ProtocolTypeResolver(List<ProtocolController> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void afterPropertiesSet() {
        log.info("protocol types count: {}", ProtocolType.values().length);
        log.info("controllers count: {}", controllers.size());
        /**
         * todo : validate, route
         *  validate
         *   1. controller 내 메서드와 매칭 되는 protocolType 이 없는지 검사
         *   2. 1:1 이상의 메서드와 매칭되는 type 이 없는지 검사
         *  route
         *   1. controller 내 ProtocolType 을 메서드와 매칭해주기 위한 annotation 추가
         *   2. 매칭되는 메서드를 controller 내에서 찾아서 적당한 자료구조로 (Function?) map 으로 만들어 두기
         *
         *   ** bytes 에서 type 을 추출할 수 있어야 함, header 를 정의해야하나? 필요하면 할것
         */
    }
}
