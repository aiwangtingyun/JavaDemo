package com.wang.nio;


import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPathsAndFiles {

    /**
     * Paths 提供的 get() 方法用来获取 Path 对象：
     * 			Path get(String first, String … more) : 用于将多个字符串串连成路径。
     *
     * 		Path 常用方法：
     * 			boolean endsWith(String path) : 判断是否以 path 路径结束
     * 			boolean startsWith(String path) : 判断是否以 path 路径开始
     * 			boolean isAbsolute() : 判断是否是绝对路径
     * 			Path getFileName() : 返回与调用 Path 对象关联的文件名
     * 			Path getName(int idx) : 返回的指定索引位置 idx 的路径名称
     * 			int getNameCount() : 返回Path 根目录后面元素的数量
     * 			Path getParent() ：返回Path对象包含整个路径，不包含 Path 对象指定的文件路径
     * 			Path getRoot() ：返回调用 Path 对象的根路径
     * 			Path resolve(Path p) :将相对路径解析为绝对路径
     * 			Path toAbsolutePath() : 作为绝对路径返回调用 Path 对象
     * 			String toString() ： 返回调用 Path 对象的字符串表示形式
     */
    @Test
    public void testPaths() {
        Path path = Paths.get("D:/", "图片/girl.png");
        System.out.println(path.endsWith("girl.png"));
        System.out.println(path.startsWith("D:/"));
        System.out.println(path.isAbsolute());
        System.out.println(path.getFileName());
        System.out.println(path.getParent());
        System.out.println(path.getRoot());
        System.out.println(path.toString());
        for (int i = 0; i < path.getNameCount(); i++) {
            System.out.println(path.getName(i));
        }
    }

    @Test
    public void testFiles() {
        // 后续补充完整
    }
}
