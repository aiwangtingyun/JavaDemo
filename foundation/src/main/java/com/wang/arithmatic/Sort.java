package com.wang.arithmatic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sort {

    // 冒泡排序
    public static int[] bubbleSort(int[] array) {
        if (array == null || array.length == 0)
            return array;

        int temp;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j + 1] < array[j]) {
                    temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                }
            }
        }

        return array;
    }

    // 选择排序
    public static int[] selectionSort(int[] array) {
        if (null == array || array.length == 0) {
            return array;
        }
        int temp;
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[minIndex]) {   // 找到最小的数
                    minIndex = j;                   // 保存最小数的索引
                }
            }
            temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }

    // 插入排序
    public static int[] insertionSort(int[] array) {
        if (null == array || array.length == 0) {
            return array;
        }
        int current, preIndex;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }

    @Test
    public void testSort() {
        int[] array = new int[]{1, 3, 5, 2, 11, 6, 8, 23, 7, 18};

        Sort.insertionSort(array);

        System.out.println(Arrays.toString(array));
    }
}
