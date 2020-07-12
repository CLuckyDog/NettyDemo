package com.nio.channel.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        /*得到一个网络通道*/
        SocketChannel socketChannel = SocketChannel.open();
        /*设置非阻塞*/
        socketChannel.configureBlocking(false);
        /*提供服务器端的IP 和端口*/
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        /*连接服务器*/
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞！我可以做其他的事情！");
            }
        }
        /*如果连接成功，就发送数据*/
        String info="我要学netty！";
        ByteBuffer byteBuffer = ByteBuffer.wrap(info.getBytes());
        /*发送数据，就是把buffer里的数据写到channel中*/
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
