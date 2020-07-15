package com.wang.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/*
 * 一、创建执行线程的方式三：实现 Callable 接口。 相较于实现 Runnable 接口的方式，方法可以有返回值，并且可以抛出异常。
 *
 * 二、执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果。  FutureTask 是  Future 接口的实现类
 */
public class TestCallable {

    public static void main(String[] args) {
        CallableDemo demo = new CallableDemo(100);

        //1.执行 Callable 方式，需要 FutureTask 实现类的支持，用于接收运算结果
        FutureTask<Integer> futureTask = new FutureTask<>(demo);

        new Thread(futureTask).start();

        try {
            Integer num = futureTask.get();
            System.out.println(num);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class CallableDemo implements Callable<Integer> {

    private int count = 0;

    public CallableDemo(int count) {
        this.count = count;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;

        for (int i = 0; i < count; i++) {
            sum += i;
        }

        return sum;
    }
}
