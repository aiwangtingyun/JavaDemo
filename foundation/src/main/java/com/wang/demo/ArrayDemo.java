package com.wang.demo;

import java.util.Arrays;

public class ArrayDemo {

    public static void main(String[] args) {
        // 静态初始化：数组的初始化和赋值同时进行
        int[] a = new int[]{1, 2, 3, 4, 5};

        // 动态初始化：数组的初始化和赋值分开进行
        int[] b = new int[5];
        for (int i = 1; i <= b.length; i++) {
            b[i-1] = i;
        }

        // 类型推断
        int[] c = {1, 2, 3, 4, 5};

        // 二位数组
        int[][] arr1 = new int[][]{{1, 2, 3}, {4, 5}};
        String[][] arr2 = new String[3][2];

        System.out.println(Arrays.toString(a) + " length: " + a.length);
        System.out.println(Arrays.toString(b) + " length: " + b.length);
        System.out.println(Arrays.toString(c) + " length: " + c.length);
    }
}
