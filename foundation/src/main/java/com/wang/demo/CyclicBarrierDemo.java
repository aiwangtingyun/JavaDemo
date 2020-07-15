package com.wang.demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                String name = Thread.currentThread().getName();
                System.out.println(name + " 开始启动");
                try {
                    cyclicBarrier.await();
                    System.out.println("第一次到达的线程：" + name);

                    cyclicBarrier.await();
                    System.out.println("第二次到达的线程：" + name);

                    cyclicBarrier.await();
                    System.out.println("第三次到达的线程：" + name);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
