package com.wang.demo;

public class SingletonDemo {

    // 使用一个类变量来缓存实例
    private static SingletonDemo instance;

    // 隐藏构造器
    private SingletonDemo() { }

    // 提供一个静态方法用于返回 Singleteon 实例
    public static SingletonDemo getInstance() {
        if (instance == null) {
            instance = new SingletonDemo();
        }
        return instance;
    }

    public static void main(String[] args) {
        SingletonDemo s1 = SingletonDemo.getInstance();
        SingletonDemo s2 = SingletonDemo.getInstance();
        System.out.println(s1 == s2);
    }
}
