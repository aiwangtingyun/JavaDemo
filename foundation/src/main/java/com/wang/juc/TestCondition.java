package com.wang.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 编写一个程序，开启 3 个线程，这三个线程的 ID 分别为 A、B、C，每个线程将自己的 ID 在屏幕上打印 10 遍，
 * 要求输出的结果必须按顺序显示。 如：ABCABCABC…… 依次递归
 *
 * 实现方法：使用锁的条件量 Condition
 */
public class TestCondition {

    public static void main(String[] args) {
        AlternateDemo demo = new AlternateDemo();

        // A 线程打印 'B'
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    demo.loopA();
                }
            }
        }, "A").start();

        // B 线程打印 'B'
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    demo.loopB();
                }
            }
        }, "B").start();

        // C 线程打印 'C'
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    demo.loopC();
                }
            }
        }, "C").start();
    }
}

class AlternateDemo {

    // 当前线程正在执行的标记
    private int number = 1;

    // 创建一个锁和三个条件量
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    public void loopA() {
        // 加锁
        lock.lock();

        try {
            // 1、判断
            if (number != 1) {
                condition1.await();
            }

            // 2、打印
            System.out.print(Thread.currentThread().getName());

            // 3、唤醒第2个线程
            number = 2;
            condition2.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public void loopB() {
        // 加锁
        lock.lock();

        try {
            // 1、判断
            if (number != 2) {
                condition2.await();
            }

            // 2、打印
            System.out.print(Thread.currentThread().getName());

            // 3、唤醒第3个线程
            number = 3;
            condition3.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    public void loopC() {
        // 加锁
        lock.lock();

        try {
            // 1、判断
            if (number != 3) {
                condition3.await();
            }

            // 2、打印
            System.out.print(Thread.currentThread().getName());

            // 3、唤醒第1个线程
            number = 1;
            condition1.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
