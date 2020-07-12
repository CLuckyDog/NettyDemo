package com.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server {
    /*定义属性*/
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static  final int PORT=6667;

    /*构造器 初始化工作*/
    public Server() {
        try {
            /*获取选择器*/
            selector=Selector.open();
            /*ServerSocketChannel*/
            listenChannel=ServerSocketChannel.open();
            /*绑定端口*/
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            /*设置非阻塞模式*/
            listenChannel.configureBlocking(false);
            /*将channel注册到selector*/
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*监听*/
    public void listen() {
        while (true){
            try {
//                int count = selector.select(2000);
                int count = selector.select();
                if(count>0){/*有事件处理*/
                    /*遍历得到selectionKey集合*/
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        /*监听到accept*/
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            /*将sc注册到selector*/
                            sc.register(selector,SelectionKey.OP_READ);
                            /*提示*/
                            System.out.println(sc.getRemoteAddress()+"online.....");
                        }
                        if (key.isReadable()){
                            /*客户端发来数据*/
                            /*处理读工作  单独写个方法调用*/
                            readData(key);
                        }

                        /*把当前的key删除，防止重复处理*/
                        iterator.remove();
                    }
                }else{
                    System.out.println("waitting.....");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {

            }
        }
    }

    /*读取客户端消息*/
    private void readData(SelectionKey key)  {
        /*定义一个socketChannel*/
        SocketChannel channel=null;
        try {
            /*取到关联的channel*/
            channel= (SocketChannel) key.channel();
            /*创建buffer*/
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            /*根据read的值进行处理*/
            if (read>0){
                /*把缓冲区的数据转成字符串*/
                String msg = new String(buffer.array(),0,read);
                /*输出消息*/
                System.out.println("from client:"+msg);
                /*向其他客户端转发消息(排除自己)，专门写一个方法转发消息*/
                sendInfoToOthers(msg,channel);
            }
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress()+"offline.....");
                /*取消注册*/
                key.cancel();
                /*关闭通道*/
                channel.close();
            } catch (IOException ex) {

            }
        }

    }
    /*转发消息到其他client*/
    private void sendInfoToOthers(String msg,SocketChannel selfChannel) throws IOException {
        System.out.println("Server 转发消息中........");
        /*遍历 所有注册到selector上的socketChannel 并排除自己*/
        for (SelectionKey key:selector.keys()){
            /*通过key取出对应的channel*/
            Channel targetChannel =key.channel();
            /*排除自己*/
            if (targetChannel instanceof SocketChannel && targetChannel != selfChannel){
                SocketChannel dest= (SocketChannel) targetChannel;
                /*将msg存储到buffer*/
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                /*将buffer的数据写入通道中*/
                dest.write(buffer);
            }

        }
    }
    public static void main(String[] args) {
        /*创建一个服务器对象*/
        Server server = new Server();
        server.listen();
    }
}
