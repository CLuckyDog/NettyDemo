package com.nio.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    /*定义一个channel组，管理所有的channel*/
    /*GlobalEventExecutor.INSTANCE 是全局的时间执行器，是一个单例*/
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /*handlerAdded  表示连接建立，一旦连接，第一个被执行的方法
    * 将当前这个channel加入到 channelGroup
    * */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        /*将该客户端上线的消息推送给其它一上线的客户端*/
        /*该方法会将channelGroup中所有的channel遍历，并发送消息，因此我们自己无需再次遍历*/
        channelGroup.writeAndFlush(sdf.format(new Date())+":[客户端]"+channel.remoteAddress()+"加入聊天！\n");
        channelGroup.add(channel);
    }

    /*表示channel处于活跃状态，提示xx上线*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(sdf.format(new Date())+":"+ctx.channel().remoteAddress()+"上线了！");
    }

    /*表示channel处于不活跃状态，提示 xx离线*/
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(sdf.format(new Date())+":"+ctx.channel().remoteAddress()+"下线了！");
    }

    /*断开连接,将xx客户端离开的消息推送给当前在线的客户端*/
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(sdf.format(new Date())+":"+"[客户端]"+channel.remoteAddress()+"离开了！\n");
        System.out.println("channelGroup size"+channelGroup.size());
    }

    /*读取数据*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        /*获取到当前channel*/
        Channel channel = ctx.channel();
        /*这时我们遍历channelGroup，根据不同的情况，回复不同的消息*/
        channelGroup.forEach(ch ->{
            if (channel != ch){
                ch.writeAndFlush(sdf.format(new Date())+":"+"[客户端]"+channel.remoteAddress() +"发送消息："+msg+"\n");
            }else {
                ch.writeAndFlush(sdf.format(new Date())+":"+"[自己]发送了消息："+msg+"\n");
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        /*发生异常，关闭通道*/
        ctx.close();
    }
}
