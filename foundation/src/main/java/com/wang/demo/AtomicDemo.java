package com.wang.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {
    class Count {
        private AtomicInteger count = new AtomicInteger(0);

        public int getCount() {
            return count.get();
        }

        public void increCount() {
            count.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();

        AtomicDemo demo = new AtomicDemo();
        Count count = demo.new Count();

        // 100 个线程对共享变量进行加1
        for (int i = 0; i < 100; i++) {
            service.submit(() -> count.increCount() );
        }

        // 等待线程结束
        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 打印结果
        System.out.println("Count : " + count.getCount());
    }
}
