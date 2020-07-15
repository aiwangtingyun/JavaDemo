package com.wang.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 测试数据报的非阻塞通道
 */
public class TestNonBlockingNIO2 {

    // 发送端
    public static void main(String[] args) throws IOException {
        DatagramChannel sendChannel = DatagramChannel.open();

        sendChannel.configureBlocking(false);

        Scanner scanner = new Scanner(System.in);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 8899);

        while (scanner.hasNext()) {
            String data = scanner.next();
            buffer.put((new Date() + "\n" + data).getBytes());
            buffer.flip();
            sendChannel.send(buffer, socketAddress);
            buffer.clear();
        }

        sendChannel.close();
    }

    // 接收端
    @Test
    public void receiver() throws IOException{
        DatagramChannel recvChannel = DatagramChannel.open();

        recvChannel.configureBlocking(false);

        recvChannel.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();

        recvChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                    recvChannel.receive(buffer);
                    buffer.flip();
                    System.out.println(">>> " + new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }
    }
}
