package com.wang.curd;


import com.wang.bean.Customer;
import com.wang.bean.Order;
import com.wang.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 PreparedStatement 实现对不同表的通用查询操作
 */
public class PreparedStatementQuery {

    // 测试查询功能
    @Test
    public void testQuery() {
        // 测试单条查询结果
        String sql = "select `id`, `name`, `email`, `birth` from customers where id = ?;";
        System.out.println(getInstance(Customer.class, sql, 2));
        System.out.println("---------------");
        sql = "select `order_id` orderId, `order_name` orderName, `order_date` orderDate " +
                "from `order` where order_id = ?;";
        System.out.println(getInstance(Order.class, sql, 4));
        System.out.println("---------------");

        // 测试多条查询结果
        sql = "select `id`, `name`, `email`, `birth` from customers where id < ?;";
        List<Customer> customer = getInstanceList(Customer.class, sql, 4);
        customer.forEach(System.out::println);
        System.out.println("---------------");
        sql = "select `order_id` orderId, `order_name` orderName, `order_date` orderDate " +
                "from `order` where order_id < ?;";
        List<Order> orders = getInstanceList(Order.class, sql, 5);
        orders.forEach(System.out::println);
    }


    // 获取查询结果表中的一条记录
    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1、获取 Connection 连接对象
            conn = JDBCUtils.getConnect();

            // 2、构建 PreparedStatement 对象
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 3、执行查询并获取结果
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            // 4、构建结果对象
            if (rs.next()) {
                T t = clazz.newInstance();
                int columnCount = rsmd.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnVal = rs.getObject(i + 1);

                    // 获取列别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 对象属性赋值
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

    // 获取查询结果表中的所有记录
    public <T> List<T> getInstanceList(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1、获取 Connection 连接对象
            conn = JDBCUtils.getConnect();

            // 2、构建 PreparedStatement 对象
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 3、执行查询并获取结果
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // 4、构建结果对象列表
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnVal = rs.getObject(i + 1);

                    // 获取列别名
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 对象属性赋值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnVal);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
