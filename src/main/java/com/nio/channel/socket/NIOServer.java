package com.nio.channel.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer  {
    public static void main(String[] args) throws IOException {
        /*创建ServerSocketChannel -> SocketChannel*/
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        /*绑定一个端口6666，在服务器端监听*/
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        /*设置为非阻塞*/
        serverSocketChannel.configureBlocking(false);

        /*得到一个Selector对象*/
        Selector selector = Selector.open();

        /*把serverSocketChannel 注册到 selector 关注事件为OP_ACCEPT*/
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        /*循环等待客户端连接*/
        while (true){
            /*这里我们等待一秒，如果没有事件发生，就返回*/
            if (selector.select(1000)==0){
                System.out.println("服务器等待了1秒钟，无连接！");
                continue;
            }
            /*如果返回的>0  就获取到相关的 selectiongkeys集合*/
            /*1、如果返回的>0 表示已经获取到关注的事件了
            *   2、selector.selectedKeys() 返回关注事件的集合
            *         通过selectionKeys反向获取通道
            * */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            /*遍历Set<SelectionKey>，使用迭代器遍历*/
            Iterator<SelectionKey> keyIterator= selectionKeys.iterator();

            while(keyIterator.hasNext()){
                /*获取SelectionKey*/
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()){/*如果是 OP_ACCEPT,有新的客户端连接上*/
                    /*该客户端生成一个SocketChannel 此处是不阻塞的，以往的在这里会发生阻塞*/
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel："+socketChannel.hashCode());
                    /*将socketChannel设置为非阻塞模式*/
                    socketChannel.configureBlocking(false);
                    /*将当前的socketChannel  注册到selector上*/
                    /*同时给socketChannel关联一个Buffer*/
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()){/*发生 OP_READ*/
                    /*首先通过key反向获取到对应的channel*/
                    SocketChannel channel = (SocketChannel)key.channel();
                    /*获取到该channel关联的buffer*/
                    ByteBuffer buffer = (ByteBuffer)key.attachment();
                    int read = channel.read(buffer);
                    System.out.println("from client:"+new String(buffer.array(),0,read));
                }
                /*手动从集合中移除当前的selectionKey，防止重复操作*/
               keyIterator.remove();
            }
        }


    }
}
