package com.wang.juc;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
 * 1. ReadWriteLock : 读写锁
 *
 * 写写/读写 需要“互斥”
 * 读读 不需要互斥
 *
 */
public class TestReadWriteLock {

    public static void main(String[] args) {
        ReadWriteLockDemo lockDemo = new ReadWriteLockDemo();

        // 一个线程写
        new Thread(new Runnable() {
            @Override
            public void run() {
                lockDemo.write((int)(Math.random() * 101));
            }
        }, "Write").start();

        // 10个线程读
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lockDemo.read();
                }
            }).start();
        }
    }
}

class ReadWriteLockDemo {

    private int number = 0;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // 读
    public void read() {
        // 加读锁
        lock.readLock().lock();

        try {
            System.out.println(Thread.currentThread().getName() + " read : " + number);
        } finally {
            // 释放读锁
            lock.readLock().unlock();
        }
    }

    // 写
    public void write(int number) {
        // 加写锁
        lock.writeLock().lock();

        try {
            this.number = number;
            System.out.println(Thread.currentThread().getName() + " write : " + number);
        } finally {
            // 释放读锁
            lock.writeLock().unlock();
        }
    }
}
