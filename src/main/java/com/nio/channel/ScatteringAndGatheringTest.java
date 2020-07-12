package com.nio.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering（分散）:将数据写入buffer时，可以采用buffer数组，依次写入
 * Gathering（聚合）:从buffer读取数据时候，可以采用buffer数组，依次读出
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        /*使用ServerSocketChannel 和 SocketChannel 网络*/
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        /*绑定端口到socket，并启动*/
        serverSocketChannel.socket().bind(inetSocketAddress);

        /*创建buffer数组*/
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        /*等待客户端连接*/
        SocketChannel accept = serverSocketChannel.accept();
        int msgLength=8;/*假定从客户端接受8个字节*/
        /*循环读取*/
        while (true){
            int byteRead =0;
            while (byteRead<msgLength){
                    long read=accept.read(byteBuffers);
                    byteRead+=read;
                System.out.println("byteRead="+byteRead);
                /*使用流打印，看看当前的这个buffer的position 和 limit*/
                Arrays.asList(byteBuffers).stream().map(buffer->"position="+buffer.position()+",limit="+buffer.limit())
                    .forEach(System.out::println);
            }

            /*将所有的buffer进行flip翻转*/
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            /*将数据读出显示到客户端*/
            long byteWrite=0;
            while (byteWrite<msgLength){
                long write = accept.write(byteBuffers);/*回写到客户端*/
                byteWrite+=write;
            }

            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

            System.out.println("byteRead="+byteRead+",byteWrite="+byteWrite+"msgLength="+msgLength);
        }


    }
}
