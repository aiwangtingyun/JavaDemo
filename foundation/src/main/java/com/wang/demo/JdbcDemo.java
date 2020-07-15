package com.wang.demo;

import java.sql.*;

public class JdbcDemo {
    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // 加载驱动包
            Class.forName("com.mysql.jdbc.Driver");

            // 数据库地址、用户名、密码
            String databaseUrl = "jdbc:mysql//localhost:3306/school";
            String userName = "root";
            String password = "123465";

            // 获取与数据库连接对象
            connection = DriverManager.getConnection(databaseUrl, userName, password);

            // 创建执行 sql 语句的 statememt 对象
            statement = connection.createStatement();

            // 执行 sql 语句
            resultSet = statement.executeQuery("select * from students");

            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源，后调用的先关闭，关闭之前先检查
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
