package com.wang.juc;

import java.util.concurrent.CountDownLatch;

/*
 * CountDownLatch ：闭锁，在完成某些运算是，只有其他所有线程的运算全部完成，当前运算才继续执行
 */
public class TestCountDownLatch {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);
        LatchDemo latchDemo = new LatchDemo(latch);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 10; i++) {
            new Thread(latchDemo).start();
        }

        try {
            // 等待锁计数完毕
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.out.println("耗费时间为：" + (end - start));
    }
}

class LatchDemo implements Runnable {

    private CountDownLatch latch;

    public LatchDemo(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5000; i++) {
                if (i % 10 == 0) {
                    System.out.println(i);
                }
            }
        } finally {
            // 运行完后进行一次锁计数
            latch.countDown();
        }
    }
}
