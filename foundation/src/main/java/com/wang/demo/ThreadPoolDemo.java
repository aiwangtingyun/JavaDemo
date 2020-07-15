package com.wang.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService service = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor)service;

        //  执行指定线程，需要提供 Runnable 接口或 Callable 接口实现类的对象
        threadPool.execute(new MyRunable());

        // 关闭线程池
        threadPool.shutdown();
    }
}

class MyRunable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + " : " + i);
            }
        }
    }
}

