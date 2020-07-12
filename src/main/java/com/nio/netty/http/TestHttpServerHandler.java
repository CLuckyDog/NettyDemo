package com.nio.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.util.CharsetUtil.UTF_8;

/*
* 说明
* 1.SimpleChannelInboundHandler 是   ChannelInboundHandlerAdapter 的子类
* 2.HttpObject  表示客户端和服务器端相互通信的数据被封装成HttpObject类型
* */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /*channelRead0  读取客户端数据*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        /*判断 msg 是不是 httpRequest请求*/
        if (msg instanceof HttpRequest){
            System.out.println("msg 类型："+msg.getClass());
            System.out.println("客户端地址："+ctx.channel().remoteAddress());

            System.out.println("pipeline hashcode="+ctx.pipeline().hashCode());
            System.out.println("TestHttpServerHandler hashcode="+this.hashCode());

            /*获取request*/
            HttpRequest  request= (HttpRequest) msg;
            /*获取URI 过滤指定的资源*/
            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) return;


            /*回复信息给浏览器 http协议*/
            ByteBuf content= Unpooled.copiedBuffer("hello,我是netty http 服务器",UTF_8);
            /*构造一个http的响应，即 httpResponse*/
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=utf-8");
            response.headers().set(CONTENT_LENGTH,content.readableBytes());

            /*构建好response返回*/
            ctx.writeAndFlush(response);

        }
    }
}
