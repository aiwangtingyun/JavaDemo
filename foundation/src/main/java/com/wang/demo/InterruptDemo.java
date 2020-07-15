package com.wang.demo;

public class InterruptDemo {
    // runnable 对象
    Runnable runnable = () -> {
        int i = 0;
        try {
            while (i < 1000) {
                // 执行间隔为半秒钟
                Thread.sleep(500);
                System.out.println(i++ + Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            // 判断该线程是否还在
            System.out.println("current thread is alive : " + Thread.currentThread().isAlive());

            // 判断该线程的中断标志位状态
            System.out.println("current thread isInterrupted : " + Thread.currentThread().isInterrupted());

            System.out.println("In Runnable");
            e.printStackTrace();
        }
    };

    public static void main(String[] args) {
        InterruptDemo demo = new InterruptDemo();

        // 创建线程并启动
        Thread t1 = new Thread(demo.runnable);
        System.out.println("This is main thread!");
        t1.start();

        try {
            // 先让非主线程执行3秒钟
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("In main");
            e.printStackTrace();
        }

        // 设置中断
        t1.interrupt();
    }
}
