package com.wang.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

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
public class TestPipe {

    @Test
    public void testPipe() throws IOException {
        // 1、获取管道
        Pipe pipe = Pipe.open();

        // 2、获取 SinkChannel 和 SourceChannel 通道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        Pipe.SourceChannel sourceChannel = pipe.source();

        // 3、分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("通过单向管道发送数据".getBytes());
        buffer.flip();

        // 4、将缓冲区的数据写入管道
        sinkChannel.write(buffer);

        // 5、读取缓冲区的数据
        buffer.clear();
        sourceChannel.read(buffer);
        buffer.flip();
        System.out.println(new String(buffer.array(), 0, buffer.limit()));

        // 6、关闭通道
        sinkChannel.close();
        sourceChannel.close();
    }
}
