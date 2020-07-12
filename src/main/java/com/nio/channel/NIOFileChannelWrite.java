package com.nio.channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelWrite {
    public static void main(String[] args) throws IOException {
        String str="我要学netty！";
        /*创建一个输出流  channe*/
        FileOutputStream fos=new FileOutputStream("E:\\workspace\\NettyDemo\\TestFiles\\test01.txt");
        /*通过fos 获取对应的 FileChannel  这个fileChannel的真实类型是 FileChannelImpl*/
        FileChannel fielChannel = fos.getChannel();
        /*创建一个缓冲区 ByteBuffer*/
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        /*将str放入byteBuffer*/
        byteBuffer.put(str.getBytes());
        /*对byteBuffer 进行flip  将limit属性设置为当前的位置，并使指针返回到缓冲区的第一个位置，为了完整的读写数据*/
        /*读写数据的切换，双向都可以，读变成写，写变成读*/
        byteBuffer.flip();
        /*将byteBuffer 数据写入到fileChannel*/
        fielChannel.write(byteBuffer);
        fos.close();
    }
}
