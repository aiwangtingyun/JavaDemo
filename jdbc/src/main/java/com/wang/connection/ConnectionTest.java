package com.wang.connection;

import com.mysql.jdbc.Driver;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    // 方式一：调用第三方 API 的方式
    @Test
    public void testConnection1() throws SQLException {
        // 1、获取 Driver 驱动对象
        Driver driver = new com.mysql.jdbc.Driver();

        // 2、提供要连接的数据库: 指定字符集编码，否则会报错
        String url = "jdbc:mysql://localhost:3306/school?characterEncoding=utf-8";

        // 3、提供连接数据的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        // 4、连接数据库
        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }

    // 方式一：使用反射
    @Test
    public void testConnection2() throws Exception {
        // 1、获取 Driver 驱动对象：使用反射
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();

        // 2、提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/school?characterEncoding=utf-8";

        // 3、提供连接数据的用户名和密码
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        // 4、连接数据库
        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }

    // 使用 DriverManager
    @Test
    public void testConnection3() throws Exception {
        // 1、获取 Driver 驱动对象：使用反射
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver)clazz.newInstance();

        // 2、提供连接数据库的信息
        String url = "jdbc:mysql://localhost:3306/school?characterEncoding=utf-8";
        String user = "root";
        String password = "123456";

        // 3、注册驱动
        DriverManager.registerDriver(driver);

        // 4、获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }

    // 使用 DriverManager 但省略注册
    @Test
    public void testConnection4() throws Exception {
        // 1、提供连接数据库的信息
        String url = "jdbc:mysql://localhost:3306/school?characterEncoding=utf-8";
        String user = "root";
        String password = "123456";

        // 1、加载 Driver
        Class<?> clazz = Class.forName("com.mysql.jdbc.Driver");

        // 不需要注册驱动，因为加载类的时候 mysql 已经注册了驱动
        // Driver driver = (Driver)clazz.newInstance();
        // DriverManager.registerDriver(driver);

        // 2、获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }

    /**
     * 方式五：将数据库连接的信息声明在配置文件中
     * 好处：
     * 1、实现了数据和代码的分离，实现了解耦
     * 2、如果需要修改配置文件信息可以避免重新打包
     */
    @Test
    public void testConnection5() throws Exception {
        // 1、读取配置文件信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driver = pros.getProperty("driver");

        // 2、加载驱动
        Class.forName(driver);

        // 3、获取连接
        Connection conn = DriverManager.getConnection(url, user, password);

        System.out.println(conn);
    }
}
