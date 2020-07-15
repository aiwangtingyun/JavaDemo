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

public class TestBlockingNIO2 {
    // 客户端
    @Test
    public void client() throws IOException {
        // 获取通道：获取 socket 通道成功的时候就已经连接服务端了
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        FileChannel inChannel = FileChannel.open(Paths.get("D:/1.jpg"), StandardOpenOption.READ);

        // 发送本地数据给服务端
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (inChannel.read(buffer) != -1) {
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }

        // 关闭发送通道
        sChannel.shutdownOutput();

        // 接收服务端发来的消息
        int len = 0;
        while ((len = sChannel.read(buffer)) != -1) {
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, len));
            buffer.clear();
        }

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

        // 接收客户端发来的数据
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (clientChannel.read(buffer) != -1) {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }

        // 通知客户端数据已经接收完成
        buffer.put("数据已经接收完成！".getBytes());
        buffer.flip();
        clientChannel.write(buffer);

        // 关闭通道
        clientChannel.close();
        serverChannel.close();
        outChannel.close();
    }
}
