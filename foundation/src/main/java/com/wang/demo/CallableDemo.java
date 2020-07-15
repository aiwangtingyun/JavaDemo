package com.wang.demo;

import java.util.concurrent.*;

public class CallableDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        /* 方式一 */

        // 创建 Callable 对象
        MyCallable callable = new MyCallable(10);
        // 把 Callable 对象作为参数传递给 FutureTask 对象
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        // 把 FutureTask 对象作为参数传递给 Thread 对象并运行
        new Thread(futureTask).start();
        // 获取 Callable 对象 call() 的返回值
        Integer i = futureTask.get();
        System.out.println(i);

        /* 方式二 */

        // 创建线程池对象
        ExecutorService pool = Executors.newFixedThreadPool(2);
        // 创建可以执行 Runnable 对象或者 Callable 对象的线程
        Future<Integer> f1 = pool.submit(new MyCallable(50));
        Future<Integer> f2 = pool.submit(new MyCallable(100));
        // V get()
        Integer i1 = f1.get();
        Integer i2 = f2.get();
        // 结束线程池
        pool.shutdown();
        System.out.println("i1 : " + i1);
        System.out.println("i2 : " + i2);
    }
}

class MyCallable implements Callable<Integer> {
    private int number;

    public MyCallable(int number) {
        this.number = number;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i <= this.number; i++) {
            sum += i;
        }
        return sum;
    }
}
