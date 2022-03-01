package com.tkbro.noobmatch.server;

import com.tkbro.noobmatch.handler.ApiServerHandler;
import com.tkbro.noobmatch.handler.ExampleHandler;
import com.tkbro.noobmatch.model.codec.Api2MatchProtocolCodec;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    // apiServerHandler 테스트를 위해 autowired 를 쓰느라 임시로 추가했다.
    // 최대한 bean 과 무관한 상태로 만들도록 해보고 지우자.
    @Autowired
    ConfigurableApplicationContext applicationContext;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                // 임시 codec 구현 후 동작하지 않아서 일단 주석처리, netty pipeline 동작 순서 고려해서 수정하거나 아예 제거 필요
//                .addLast("decoder", new StringDecoder(CharsetUtil.UTF_8))
//                .addLast("encoder", new StringEncoder(CharsetUtil.UTF_8))
//                .addLast(new ExampleHandler())
                .addLast(new Api2MatchProtocolCodec())
                .addLast(applicationContext.getBean(ApiServerHandler.class));
    }
}
