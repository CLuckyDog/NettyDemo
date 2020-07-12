package com.nio.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        /*创建BossGroup   和   WorkerGroup*/
        /*
        *   说明
        *   1.创建两个线程组，bossGroup和workerGroup
        *   2.bossGroup只是处理连接请求，真正的和客户端处理业务，会交给workerGroup完成
        *   3.两个都是无限循环
        *   4.bossGroup和workerGroup 含有的子线程（NioEventLoop）的个数
        *       默认是CPU的核数*2
        * */
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        /*每一个线程，都有个selector，轮训pipeline，检测是否有事件发生*/
        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {
            /*创建服务器端的启动对象，配置参数*/
            ServerBootstrap bootstrap = new ServerBootstrap();

            /*使用链式编程来进行配置*/
            bootstrap.group(bossGroup,workerGroup)  /*设置两个线程组*/
                    .channel(NioServerSocketChannel.class)    /*使用NioServerSocketChannel作为服务器的通道实现*/
                    .option(ChannelOption.SO_BACKLOG,128)   /*设置线程队列得到连接个数*/
                    .childOption(ChannelOption.SO_KEEPALIVE,true)   /*设置保持活动连接状态*/
                    .handler(null)  /*handler 对应bossGroup childHandler 对应 workerGroup*/
                    .childHandler(new ChannelInitializer<SocketChannel>() {/*创建一个通道测试对象*/
                        /*给PipeLine设置处理器*/
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            System.out.println("客户端socketChannel hashCode = "+ch.hashCode());
                            /*
                            * 可以使用一个集合管理SocketChannel，再推送消息时，可以将业务加入到各个channel对应的
                            * NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            * */
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); /*给我们的workerGroup 的 EventLoop 对应的管道设置处理器*/

            System.out.println("---------服务器 is ready--------");

            /*绑定一个端口并且同步，生成了一个ChannelFuture对象
            * 绑定端口并启动服务
            * */
            ChannelFuture cf = bootstrap.bind(6668).sync();

            cf.addListener((ChannelFuture future)->{
                if (cf.isSuccess()){
                    System.out.println("监听端口 6668 成功！");
                }else{
                    System.out.println("监听端口 6668 失败！");
                }
            });

            /*对关闭通道进行监听*/
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
