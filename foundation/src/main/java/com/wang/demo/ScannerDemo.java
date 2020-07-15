package com.wang.demo;

import java.util.Scanner;

public class ScannerDemo {

    public static void main(String[] args) {

        // 使用 Scanner 从键盘中获取不同类型的变量
        Scanner scanner = new Scanner(System.in);

        // 调用 Scanner 类的各种相关方法
        System.out.println("请输入你的名字：");
        String name = scanner.next();
        System.out.println("name: " + name);

        System.out.println("请输入你的年龄：");
        int age = scanner.nextInt();
        System.out.println("age: " + age);

        System.out.println("请输入你的体重：");
        double weight = scanner.nextDouble();
        System.out.println("weiget: " + weight);

        System.out.println("你的性别是：");
        String sex = scanner.next();
        System.out.println("sex: " + sex);

        System.out.println("你是否喜欢我？(true/false)");
        boolean love = scanner.nextBoolean();
        System.out.println("love: "  + love);
    }
}
