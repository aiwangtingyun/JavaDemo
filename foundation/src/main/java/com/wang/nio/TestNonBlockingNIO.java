package com.wang.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

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
public class TestNonBlockingNIO {

    // 客户端
    public static void main(String[] args)  throws IOException {
        // 1、获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8989));

        // 2、切换为非阻塞模式
        sChannel.configureBlocking(false);

        // 3、分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 4、发送数据给服务端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            buffer.put((new Date() + "\n" + next).getBytes());
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }

        // 5、关闭通道
        scanner.close();
    }

    // 服务端
    @Test
    public void server() throws IOException {
        // 1、获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2、切换非阻塞模式
        ssChannel.configureBlocking(false);

        // 3、绑定连接
        ssChannel.bind(new InetSocketAddress(8989));

        // 4、获取选择器
        Selector selector = Selector.open();

        // 5、将通道注册到选择器上，并指定"监听接收事件"
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6、轮询式的获取选择器上已经"准备就绪"的事件
        while (selector.select() > 0) {
            // 7、获取当前选择器中所有已就绪的监听事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                // 8、获取准备就绪的事件
                SelectionKey key = iterator.next();

                // 9、判断具体是什么事件准备就绪
                if (key.isAcceptable()) {
                    // 如果就绪事件为"接收就绪"，则获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();
                    // 设置为非阻塞模式
                    sChannel.configureBlocking(false);
                    // 同时把通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel = (SocketChannel) key.channel();
                    // 读取客户端数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = sChannel.read(buffer)) > 0) {
                        buffer.flip();
                        System.out.println(">>> " + new String(buffer.array(), 0, len));
                        buffer.clear();
                    }
                }
                // 10、移除已经使用的选择键
                iterator.remove();
            }
        }
    }
}
