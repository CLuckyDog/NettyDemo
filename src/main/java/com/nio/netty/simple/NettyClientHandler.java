package com.nio.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static io.netty.util.CharsetUtil.UTF_8;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    /*当通道就绪就会触发该方法：发送消息*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client:"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello , server:喵",UTF_8));
    }

    /*当通道有读取事件时，会触发读取服务器端返回的数据*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf= (ByteBuf) msg;
        System.out.println("服务器回复的消息："+buf.toString(UTF_8));
        System.out.println("服务器的地址："+ctx.channel().remoteAddress());
    }

    /*处理异常一般是需要关闭通道*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
