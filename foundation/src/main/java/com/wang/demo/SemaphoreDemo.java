package com.wang.demo;

import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
    public static void main(String[] args) {
        // 控制同时访问的线程个数为10
        Semaphore semaphore = new Semaphore(10);

        for (int i = 0; i < 100; i++) {
            int finalI = i;     // lambda或者内部类使用了局部变量后会变成final类型
            new Thread(() -> {
                try {
                    // 获取信号量许可证
                    semaphore.acquire();
                    System.out.println(finalI + " 得到了信号量许可证...");

                    // 执行其他操作
                    Thread.sleep(1000);

                    // 释放信号量许可证
                    System.out.println(finalI + " 释放了信号量许可证...");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
