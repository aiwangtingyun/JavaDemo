package com.wang.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 一、使用 NIO 完成网络通信的三个核心：
 *
 * 1. 通道（Channel）：负责连接
 *
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 *
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 *
 * 2. 缓冲区（Buffer）：负责数据的存取
 *
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 *
 */
public class TestBlockingNIO {

    // 客户端
    @Test
    public void client() throws IOException {
        // 获取通道：获取 socket 通道成功的时候就已经连接服务端了
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        FileChannel inChannel = FileChannel.open(Paths.get("D:/1.jpg"), StandardOpenOption.READ);

        System.out.println("连接服务器成功 ------- ");

        // 发送本地数据给服务端
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (inChannel.read(buffer) != -1) {
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }

        System.out.println("发送数据完成 ------- ");

        // 关闭通道
        inChannel.close();
        sChannel.close();
    }

    // 服务端
    @Test
    public void server() throws IOException {
        // 获取通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("D:/2.jpg"),
                                StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 绑定连接
        serverChannel.bind(new InetSocketAddress(9898));

        // 获取客户端连接的通道
        SocketChannel clientChannel = serverChannel.accept();

        System.out.println("获取客户端连接通道成功 ------- ");

        // 接收客户端发来的数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (clientChannel.read(buffer) != -1) {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }

        System.out.println("接收客户端发送的数据成功 ------- ");

        // 关闭通道
        clientChannel.close();
        serverChannel.close();
        outChannel.close();
    }
}
