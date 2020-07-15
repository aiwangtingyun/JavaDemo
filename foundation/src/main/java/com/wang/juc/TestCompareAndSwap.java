package com.wang.juc;

/*
 * 模拟 CAS 算法
 */
public class TestCompareAndSwap {

    public static void main(String[] args) {
        final CompareAndSwap cas = new CompareAndSwap();

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int expectValue = cas.getValue();
                    boolean b = cas.compareAndSet(expectValue, (int) (Math.random() * 101));
                    System.out.println(b + " " + cas.getValue());
                }
            }).start();
        }
    }
}

class CompareAndSwap {

    private int value;

    // 获取内存值
    public synchronized int getValue() {
        return value;
    }

    // 比较
    public synchronized int compareAndSwap(int expectValue, int newValue) {
        int oldValue = value;

        // 只有内存值和期待值一致时才把内存值设置为新值，否则什么也不做
        if (expectValue == oldValue) {
            this.value = newValue;
        }

        return oldValue;
    }

    // 设置
    public synchronized boolean compareAndSet(int expectValue, int newValue) {
        return expectValue == compareAndSwap(expectValue, newValue);
    }
}
