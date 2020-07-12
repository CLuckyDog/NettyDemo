package com.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Client {
    /*定义相关属性*/
    private final String HOST="127.0.0.1";
    private final int PORT=6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    /*构造器完成初始化工作*/
    public Client() throws IOException {
        selector = Selector.open();
        /*连接服务器*/
        socketChannel=socketChannel.open(new InetSocketAddress("127.0.0.1",6667));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"is ok....");
    }

    /*向服务器发送消息*/
    public void sendInfo(String info){
        info=username + "say:"+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*读取从服务器端回复的消息*/
    public void readInfo(){
        try {
            int readChannels = selector.select();
            if (readChannels>0){/*有可以用的通道*/
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){
                        /*得到相关的通道*/
                        SocketChannel channel = (SocketChannel) key.channel();
                        /*得到一个buffer*/
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        /*读取*/
                        int read = channel.read(byteBuffer);
                        /*把读到缓冲区的数据转成字符串*/
                        String msg = new String(byteBuffer.array(), 0, read);
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();/*删除当前的selectionKey，防止重复操作*/
            }else{
//                System.out.println("没有可以用的通道。。。。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        /*启动我们的客户端*/
        Client client = new Client();
        /*启动一个线程  每隔三秒 读取从服务器端发送来的数据*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    client.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /*发送数据给服务器*/
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String info = scanner.nextLine();
            client.sendInfo(info);
        }
    }
}
