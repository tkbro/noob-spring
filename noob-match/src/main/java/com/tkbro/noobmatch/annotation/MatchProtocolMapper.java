package com.tkbro.noobmatch.annotation;

import com.tkbro.noobmatch.protocol.MatchProtocolType;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MatchProtocolMapper {
    MatchProtocolType protocolType() default MatchProtocolType.NONE;
}
