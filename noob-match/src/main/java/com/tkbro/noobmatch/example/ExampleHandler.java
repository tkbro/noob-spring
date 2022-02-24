package com.tkbro.noobmatch.example;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        logger.info("Channel Active.");
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        logger.info("message: " + msg.toString());
        context.write("ok".concat(msg.toString()).concat("\n"));
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        logger.error(cause.toString());
        cause.printStackTrace();
        context.close();
    }
}
