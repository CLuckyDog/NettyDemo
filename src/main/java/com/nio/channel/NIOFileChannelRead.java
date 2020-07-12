package com.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelRead {
    public static void main(String[] args) throws IOException {
        String str="我要学netty！";
        /*创建一个输入流  channel*/
        File file = new File("E:\\workspace\\NettyDemo\\TestFiles\\test01.txt");
        FileInputStream fis = new FileInputStream(file);

        /*通过fis 获取对应的 FileChannel  这个fileChannel的真实类型是 FileChannelImpl*/
        FileChannel fileChannel = fis.getChannel();
        /*创建一个缓冲区 ByteBuffer*/
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        /*将channe的数据读入byteBuffer*/
        fileChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fis.close();

    }
}
