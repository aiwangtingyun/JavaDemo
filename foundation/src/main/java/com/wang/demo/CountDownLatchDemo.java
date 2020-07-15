package com.wang.demo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(5);

        System.out.println("现在开始计时......");

        // 创建线池等待计数结束
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("计数结束！！！！");
            }
        }).start();

        // 创建计数线程
        new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("进行计数：" + i);
                countDownLatch.countDown();
            }
        }).start();
    }
}
