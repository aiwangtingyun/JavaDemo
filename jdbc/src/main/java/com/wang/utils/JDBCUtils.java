package com.wang.utils;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Description 操作数据库的工具类
 * @author  王廷云
 * @date 2020-05-29
 */
public class JDBCUtils {
    // 获取连接对象
    public static Connection getConnect() throws Exception {
        // 1、读取配置文件信息
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driver = pros.getProperty("driver");

        // 2、加载驱动
        Class.forName(driver);

        // 3、获取连接
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * @Description: 关闭 connection 和 Statement 的操作
     * @param: conn Connection 对象
     * @param: ps PreparedStatement 对象
     */
    public static void closeResource(Connection conn, Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 关闭 connection、Statement 和 ResultSet 的操作
     * @param: conn Connection 对象
     * @param: ps PreparedStatement 对象
     * @param: rs ResultSet 对象
     */
    public static void closeResource(Connection conn, Statement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
