package com.nio.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import java.util.concurrent.TimeUnit;

import static io.netty.util.CharsetUtil.UTF_8;

/*
*   说明：
*   1、我们自定义一个Handler  需要继承netty 规定好的某个HandlerAdapter（规范）
*       此时，我们自定义的Handler，才能称为一个Handler
* */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /*
    * 读取数据事件（这里我们可以读取客户端发送的消息）
    * 1、ChannelHandlerContext ctx:上下文对象，含有 管道pipeline，通道channel，地址
    * 2、Object msg：就是客户端发送的数据 默认Object
    * */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /*
        * 假设这里，我们有个非常耗时的操作->异步执行->提交该channel对应的
        * NIOEventLoop 的taskQueue中
        * */

        /*解决方案1：用户程序自定义的普通任务*/
        /*这里搞两个任务，时间是叠加的，因为任务在一个线程池中*/

//        ctx.channel().eventLoop().execute(()->{
//            try {
//                Thread.sleep(10000);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端~------------！",UTF_8));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(20000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端~**************！",UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        /*
        * 用户自定义定时任务 -> 该任务是提交到 scheduleTaskQueue中
        * */

        ctx.channel().eventLoop().schedule(()->{
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端~xxxxxxx！",UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },5, TimeUnit.SECONDS);

        System.out.println("go on .....");

//        System.out.println("Thread Name:"+Thread.currentThread().getName());
//        System.out.println("server ctx = "+ ctx);
//        /*channel 和 pipeline 相互包含，ctx包含两者*/
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();/*本质是一个双向链表，出站入站*/
//        /*将msg转成byteBuf*/
//        /*ByteBuf 是 Netty 提供的，不是NIO的ByteBuffer*/
//        ByteBuf buf= (ByteBuf) msg;
//        System.out.println("客户端发送消息是："+buf.toString(UTF_8));
//        System.out.println("客户端地址："+ channel.remoteAddress());
    }

    /*数据读取完毕：回复数据*/
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        /*
        * 将数据写入到缓存并刷新
        * 一般讲，我们对这个发送的数据进行编码
        * */
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello ,客户端~旺旺旺！",UTF_8));
    }

    /*处理异常一般是需要关闭通道*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
