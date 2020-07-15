package com.wang.statement;

import com.wang.bean.SqlUser;
import com.wang.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Scanner;

public class StatementTest {

    /**
     * 使用Statement的弊端：需要拼写sql语句，并且存在SQL注入的问题
     */
    public static void main(String[] args) {
        // 获取用户输入用户名和密码
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名：");
        String user = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        // 构造SQL查询语句并进行查询
        String sql = "SELECT `user`, `password` FROM user_table WHERE `user` = '" + user
                + "' AND `password` = '" + password + "'";
        SqlUser retUser = StatementTest.getInstance(sql, SqlUser.class);
        if (retUser != null) {
            System.out.println("登陆成功！" + retUser);
        } else {
            System.out.println("用户名不存在或密码错误！");
        }
    }

    // 使用Statement实现对数据表的查询操作
    public static <T> T getInstance(String sql, Class<T> clazz) {
        T t = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            // 1、获取连接对象
            conn = JDBCUtils.getConnect();

            // 2、获取statement对象
            st = conn.createStatement();

            // 3、执行查询操作
            rs = st.executeQuery(sql);

            // 4、获取查询结果数据
            ResultSetMetaData rsmd = rs.getMetaData();  // 获取结果集的元数据
            int columnCount = rsmd.getColumnCount();    // 根据结果集元数据获取结果集的列数

            // 4、构建返回对象
            if (rs.next()) {
                t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 1、获取列的名称：不建议
                    // String columnName = rsmd.getColumnName(i + 1);

                    // 1、获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 2、根据列名获取对应数据表中的数据
                    Object columnVal = rs.getObject(columnLabel);

                    // 3、将从数据表中得到的数据封装进对象中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
                return t;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, st, rs);
        }

        return null;
    }

}
