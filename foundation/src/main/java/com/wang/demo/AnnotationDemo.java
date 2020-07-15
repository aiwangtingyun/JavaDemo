package com.wang.demo;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationDemo {
    public static void main(String[] args) {
        // 获取 Student 的 Class 对象
        Class<?> clazz = Student.class;

        try {
            // 获取 Method 对象
            Method method = clazz.getMethod("study", int.class);

            // 获取注解类型元素
            if (method.isAnnotationPresent(MethodAnnotation.class)) {
                System.out.println("Student 的 study 方法上包含了 MyAnnotation 注解");
                // 打印注解类型元素
                MethodAnnotation anno = method.getAnnotation(MethodAnnotation.class);
                System.out.println("name: " + anno.name() + "age: " + anno.age() + "score: "
                        + Arrays.toString(anno.score()));
            } else {
                System.out.println("Student 的 study 方法上没有 MyAnnotation 注解");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // 获取指定注解
        DocumentA documentA = clazz.getAnnotation(DocumentA.class);
        System.out.println("documentA: " + documentA);

        // 获取所有注解，包括从父类继承下来的注解
        Annotation[] allAnn = clazz.getAnnotations();
        System.out.println("all annotation: " + Arrays.toString(allAnn));

        // 获取所有注解，但不包括从父类继承下来的注解
        Annotation[] allDeclAnn = clazz.getDeclaredAnnotations();
        System.out.println("all declared annotation: " + Arrays.toString(allDeclAnn));

        // 判断是否包含注解 DocumentA
        boolean b = clazz.isAnnotationPresent(DocumentA.class);
        System.out.println("包含 DocumentA： " + b);
    }
}

@DocumentB
class Student extends Person{
    @MethodAnnotation(name = "Andy", age = 24, score = {88, 95, 89})
    public void study(int times) {
        for (int i = 0; i < times; i++) {
            System.out.println("Good Good Study, Day Day Up!");
        }
    }
}

@DocumentA
class Person {

}

@Inherited
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface DocumentA {

}

@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface DocumentB {

}

@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@interface MethodAnnotation {
    String name();
    int age() default 18;
    int[] score();
}