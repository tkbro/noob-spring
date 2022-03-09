package com.tkbro.noobmatch.protocol;

import com.tkbro.noobmatch.annotation.MatchProtocolController;
import com.tkbro.noobmatch.annotation.MatchProtocolMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class MatchProtocolTypeResolver implements InitializingBean {

    private final Map<Byte, MatchProtocolType> protocolIdTypeMap = new HashMap<>();
    private final Map<Byte, Method> protocolHandlerMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        log.debug("protocol types count: {}", MatchProtocolType.values().length);

        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
        //scanner.addIncludeFilter(new AnnotationTypeFilter(MatchProtocolController.class));

        // for validate
        Map<Byte, Integer> validator = new HashMap<>();
        List<Method> protocolMapperMethods = new ArrayList<>();
        for (BeanDefinition beanDefinition : scanner.findCandidateComponents("com.tkbro.noobmatch")) {
            protocolMapperMethods.addAll(Arrays.stream(beanDefinition.getClass().getMethods())
                    .filter(method -> method.getAnnotation(MatchProtocolMapper.class) == null)
                    .collect(Collectors.toList()));
        }

        for (Method candidate : protocolMapperMethods) {
            MatchProtocolMapper protocolMapperAnnotation = (MatchProtocolMapper) candidate.getAnnotation(MatchProtocolMapper.class);
            if (protocolMapperAnnotation == null) {
                throw new RuntimeException("Invalid match protocol mapper method.");
            }

            MatchProtocolType protocolType = protocolMapperAnnotation.protocolType();
            int cnt = validator.get(protocolType.getProtocolId());
            if (cnt <= 0) {
                throw new RuntimeException("Duplicated match protocol mapper " +
                        "method: {}" + candidate.getName() + " type: {}" + protocolType.name());
            }

            validator.put(protocolType.getProtocolId(), cnt + 1);
            this.protocolHandlerMap.put(protocolType.getProtocolId(), candidate);
        }

        Arrays.stream(MatchProtocolType.values()).forEach(type -> {
            if (this.protocolIdTypeMap.get(type.getProtocolId()) != null) {
                // error, throw error?
                log.error("duplicate match protocol id defined");
            }

            this.protocolIdTypeMap.put(type.getProtocolId(), type);
        });
    }

    public Optional<MatchProtocolType> getMatchProtocolType(byte protocolId) {
        return Optional.ofNullable(this.protocolIdTypeMap.get(protocolId));
    }

    public Optional<Method> getMatchedTypeMethod(byte protocolId) {
        return Optional.ofNullable(this.protocolHandlerMap.get(protocolId));
    }
}
