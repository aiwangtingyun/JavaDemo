package com.wang.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 一、通道（Channel）：用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。
 *                   Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 *
 * 二、通道的主要实现类
 * 	java.nio.channels.Channel 接口：
 * 		|--FileChannel
 * 		|--SocketChannel
 * 		|--ServerSocketChannel
 * 		|--DatagramChannel
 *
 * 三、获取通道
 * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * 		本地 IO：
 * 		FileInputStream/FileOutputStream
 * 		RandomAccessFile
 *
 * 		网络IO：
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 *
 * 2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 *
 * 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 *
 * 五、分散(Scatter)与聚集(Gather)
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 *
 * 六、字符集：Charset
 * 编码：字符串 -> 字节数组
 * 解码：字节数组  -> 字符串
 *
 */
public class TestChannel {

    // 利用通道完成文件的复制（非直接缓冲区）
    @Test
    public void testFileChannel() {
        long start = System.currentTimeMillis();

        // 文件输入和输出流
        FileInputStream fis = null;
        FileOutputStream fos = null;

        // 文件通道
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            fis = new FileInputStream("D:/DNF.mp4");
            fos = new FileOutputStream("D:/DNF2.mp4");

            inChannel = fis.getChannel();
            outChannel = fos.getChannel();

            // 分配缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            // 从输入通道中读取数据到缓冲区
            while (inChannel.read(buffer) != -1) {
                // 切换缓冲区为读模式
                buffer.flip();
                // 把缓冲区数据写入到输出通道中
                outChannel.write(buffer);
                // 清空缓冲区
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inChannel != null) {
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));
    }

    // 使用直接缓冲区完成文件的复制（内存映射文件）
    @Test
    public void testFileChannel2() throws IOException {
        long start = System.currentTimeMillis();

        // 获取文件输入输出通道
        FileChannel inChannel = FileChannel.open(Paths.get("D:/DNF.mp4"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:/DNF3.mp4"),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 内存映射文件
        MappedByteBuffer inMapBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        // 直接对缓冲区进行读写操作
        byte[] bytes = new byte[inMapBuf.limit()];
        inMapBuf.get(bytes);
        outMapBuf.put(bytes);

        // 关闭通道
        inChannel.close();
        outChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));
    }

    // 通道之间的数据传输（直接缓冲区）
    @Test
    public void testChannelTransfer() throws IOException {
        // 获取文件输入输出通道
        FileChannel inChannel = FileChannel.open(Paths.get("D:/DNF.mp4"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:/DNF4.mp4"),
                StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // inChannel.transferTo(0, inChannel.size(), outChannel);
        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    // 分散读取和聚集写入
    @Test
    public void testScatterAndGatter() throws IOException {
        // 获取通道
        RandomAccessFile raf1 = new RandomAccessFile("D:/1.txt", "rw");
        FileChannel channelRead = raf1.getChannel();

        // 分配缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        // 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channelRead.read(bufs);

        for (ByteBuffer buf : bufs) {
            buf.flip();
        }
        System.out.println(new String(bufs[0].array(), 0, bufs[0].limit()));
        System.out.println("----------------");
        System.out.println(new String(bufs[1].array(), 0, bufs[1].limit()));

        // 聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("D:/2.txt", "rw");
        FileChannel channelWrite = raf2.getChannel();
        channelWrite.write(bufs);

        // 关闭资源
        channelRead.close();
        channelWrite.close();
        raf1.close();
        raf2.close();
    }

    // 查看支持的字符集
    @Test
    public void testCharset() {

        SortedMap<String, Charset> charsetMap = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> entrySet = charsetMap.entrySet();
        for (Map.Entry<String, Charset> entry : entrySet) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    /**
     * 使用字符集实现字符的编码和解码
     *      |-- 字符缓冲区 CharBuffer 编码得到字节缓冲区 ByteBuffer
     *      |-- 字节缓冲区 ByteBuffer 解码得到字符缓冲区 CharBuffer
     */
    @Test
    public void testEncodeAndDecode() throws IOException{
        // 获取字符集
        Charset gbk = Charset.forName("GBK");

        // 获取字符集对应的编码器和解码器
        CharsetEncoder encoder = gbk.newEncoder();
        CharsetDecoder decoder = gbk.newDecoder();

        // 构建字符缓冲区
        CharBuffer charBuf = CharBuffer.allocate(1024);
        charBuf.put("测试字符集编解码");
        charBuf.flip();

        // 编码
        ByteBuffer byteBuf = encoder.encode(charBuf);
        System.out.println("编码：");
        for (int i = 0; i < byteBuf.limit(); i++) {
            System.out.print(byteBuf.get() + " ");
        }

        // 解码
        byteBuf.flip();
        CharBuffer charBuf2 = decoder.decode(byteBuf);
        System.out.println("\n解码：");
        System.out.println(charBuf2.toString());
    }
}
