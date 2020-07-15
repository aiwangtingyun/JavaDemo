package com.wang.statement;

import com.wang.bean.SqlUser;
import com.wang.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class PreparedStatementTest {
    /**
     * 使用PreparedStatement的好处：
     * 1、解决了sql语句拼接和sql的注入问题
     * 2、PreparedStatement可以操作Blob的数据，而Statement做不到
     * 3、PreparedStatement可以实现更高效的批量操作
     */
    public static void main(String[] args) {
        // 获取用户输入用户名和密码
        Scanner scanner = new Scanner(System.in);

        System.out.print("请输入用户名：");
        String user = scanner.nextLine();
        System.out.print("请输入密码：");
        String password = scanner.nextLine();

        // 构造SQL查询语句并进行查询
        String sql = "SELECT `user`, `password` FROM user_table WHERE `user` = ? and `password` = ?";
        SqlUser retUser = PreparedStatementTest.getInstance(SqlUser.class, sql, user, password);
        if (retUser != null) {
            System.out.println("登陆成功！" + retUser);
        } else {
            System.out.println("用户名不存在或密码错误！");
        }
    }

    /**
     * @Description 针对于不同的表的通用的查询操作，返回表中的一条记录
     * @param clazz
     * @param sql
     * @param args
     * @return
     */
    public static <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1、获取连接对象
            conn = JDBCUtils.getConnect();

            // 2、构建prepareStatement对象
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 3、执行查询操作
            rs = ps.executeQuery();

            // 4、获取查询结果数据
            ResultSetMetaData rsmd = rs.getMetaData();  // 获取结果集的元数据
            int columnCount = rsmd.getColumnCount();    // 根据结果集元数据获取结果集的列数

            // 4、构建返回对象
            if (rs.next()) {
                T t = clazz.newInstance();
                // 处理结果集一行数据中的每一列
                for (int i = 0; i < columnCount; i++) {
                    // 1、获取列值
                    Object columnVal = rs.getObject(i + 1);

                    // 2、获取列的别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

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
            JDBCUtils.closeResource(conn, ps, rs);
        }

        return null;
    }
}
