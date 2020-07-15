package com.wang.demo;

public class ThreadDemo {
    public static void main(String[] args) {

        System.out.println("Current Thread : " + Thread.currentThread().getName());

        // 使用 Thread 类创建线程
        MyThread t1 = new MyThread();
        Thread t2 = new Thread() {
            @Override
            public void run() {
                for (int i = 1; i <= 30; i++) {
                    if (i % 20 == 0) {
                        yield();    // 放弃 CPU 执行权
                    }
                    System.out.println("data : " + i + " " + Thread.currentThread().getName());
                }
            }
        };

        // 使用 runnable 创建线程
        MyRunnable myRun = new MyRunnable();
        Thread t3 = new Thread(myRun, "RunableThread 1");
        Thread t4 = new Thread(myRun, "RunableThread 2");
        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 30; i++) {
                    System.out.println("data : " + i + " " + Thread.currentThread().getName());
                }
            }
        });

        // 测试 join 方法
        t2.start();
        for (int i = 0; i < 100; i++) {
            System.out.println("data : " + i + " " + Thread.currentThread().getName());
            if (i == 20) {
                try {
                    t2.join();  // 阻塞线程直到 t2 线程执行完毕
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

// 创建线程方式一：继承 Thread 类，重写 run 方法
class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println("data : " + i + " " + Thread.currentThread().getName());
        }
    }
}

// 创建线程方式二：实现 Runnable 接口，重写 run 方法
class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println("data : " + i + " " + Thread.currentThread().getName());
        }
    }
}
