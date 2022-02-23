package com.tkbro.noobmatch.example;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ExampleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
        System.out.println("Channel Active.");
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) throws Exception {
        System.out.println("message: " + msg.toString());
        context.write("ok");
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        cause.printStackTrace();
        context.close();
    }
}
