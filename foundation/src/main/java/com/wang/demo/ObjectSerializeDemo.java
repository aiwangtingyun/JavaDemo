package com.wang.demo;

import java.io.*;

public class ObjectSerializeDemo {
    public static void main(String[] args) {
        // 创建一个待序列化的对象
        MySerialize person = new MySerialize();
        person.setName("Andy");
        person.setAge(24);
        System.out.println(person);

        // 把对象序列化到文件中去
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("person"));
            oos.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ;
        }

        // 从文件中反序列化成对象
        File file = new File("person");
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("person"));
            MySerialize newPerson = (MySerialize) ois.readObject();
            System.out.println(newPerson);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            ;
        }
    }
}

class MySerialize implements Serializable {
    private String name;
    private transient int age;
    private static final long serialVersionUID = -6849794470754667710L;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public String toString() {
        return "Person { " +
                "name = '" + this.name + "', " +
                "age = " + this.age + " }";
    }
}
