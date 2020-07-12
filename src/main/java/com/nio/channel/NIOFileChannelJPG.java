package com.nio.channel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannelJPG {
    public static void main(String[] args) throws Exception {
        /*创建相关的流*/
        FileInputStream fis = new FileInputStream("E:\\workspace\\NettyDemo\\TestFiles\\亚索五杀.jpg");
        FileOutputStream fos = new FileOutputStream("亚索五杀-copy.jpg");

        /*获取各个流对应的fileChannel*/
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        /*使用transferFrom完成拷贝*/
        fosChannel.transferFrom(fisChannel,0,fisChannel.size());

        fosChannel.close();
        fisChannel.close();
        fos.close();
        fis.close();
    }
}
