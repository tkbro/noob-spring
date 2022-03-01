package com.tkbro.noobmatch.handler;

import com.tkbro.noobmatch.model.protocol.Api2MatchProtocol;
import com.tkbro.noobmatch.repository.ApiSessionChannelRepository;
import com.tkbro.noobmatch.service.MatchService;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ChannelHandler.Sharable
public class ApiServerHandler extends ChannelDuplexHandler {

    @Autowired
    ApiSessionChannelRepository apiSessionChannelRepository;

    // 임시
    @Autowired
    MatchService matchService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof Api2MatchProtocol) {
            Api2MatchProtocol a2mProtocol = (Api2MatchProtocol) msg;
            try {
                apiSessionChannelRepository.setChannel(ctx.channel());

                log.info("repository channel set, {}", apiSessionChannelRepository.getChannel(ctx.channel().id().asLongText()) != null);
                // 임시
                matchService.joinMatching(null);
            } finally {
                if (a2mProtocol != null && a2mProtocol.refCnt() <= 0) {
                    a2mProtocol.release();
                }
            }
        } else {
            log.error("{} Unknown message received, {}", ctx.channel(), msg);
            ReferenceCountUtil.release(msg);
        }
    }
}
