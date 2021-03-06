package com.tkbro.noobmatch.handler;

import com.tkbro.noobmatch.protocol.MatchProtocol;
import com.tkbro.noobmatch.protocol.MatchProtocolType;
import com.tkbro.noobmatch.protocol.MatchProtocolTypeResolver;
import com.tkbro.noobmatch.repository.ApiSessionChannelRepository;
import com.tkbro.noobmatch.service.MatchService;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@Slf4j
@ChannelHandler.Sharable
public class ApiServerHandler extends ChannelDuplexHandler {

    private final ApiSessionChannelRepository apiSessionChannelRepository;
    private final MatchProtocolTypeResolver matchProtocolTypeResolver;

    @Autowired
    ApiServerHandler(ApiSessionChannelRepository apiSessionChannelRepository, MatchProtocolTypeResolver matchProtocolTypeResolver) {
        this.apiSessionChannelRepository = apiSessionChannelRepository;
        this.matchProtocolTypeResolver = matchProtocolTypeResolver;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof MatchProtocol) {
            MatchProtocol protocol = (MatchProtocol) msg;
            try {
                apiSessionChannelRepository.setChannel(ctx.channel());

                log.info("repository channel set, {}", apiSessionChannelRepository.getChannel(ctx.channel().id().asLongText()) != null);

                MatchProtocolType type = (MatchProtocolType) protocol.getProtocolType();
                Method method = this.matchProtocolTypeResolver.getMatchedTypeMethod(type.getProtocolId()).orElseThrow();

                method.invoke(method.getDeclaringClass(), protocol);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception.toString());
            } catch (InvocationTargetException exception) {
                throw new RuntimeException(exception.toString());
            } finally {
                if (protocol != null && protocol.refCnt() <= 0) {
                    protocol.release();
                }
            }
        } else {
            log.error("{} Unknown message received, {}", ctx.channel(), msg);
            ReferenceCountUtil.release(msg);
        }
    }
}
