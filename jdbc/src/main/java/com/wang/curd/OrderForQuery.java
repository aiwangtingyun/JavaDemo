package com.wang.curd;

import com.wang.bean.Order;
import com.wang.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 针对于 Order 表的查询操作
 */
public class OrderForQuery {

    @Test
    public void testQueryForCustomers() {
        // 使用别名把数据表的字段名映射为Java对象属性名（ORM思想）
        String sql = "select order_id orderId, order_name orderName, order_date orderDate " +
                "from `order` where order_id = ?";

        System.out.println(queryForOrders(sql, 1));
        System.out.println(queryForOrders(sql, 2));
    }

    // 针对 Order 表的通用查询操作
    public Order queryForOrders(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnect();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            if (rs.next()) {    // 取查询结果集的一条数据记录
                Order order = new Order();
                int columnCount = rsmd.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列名（为了体现ORM思想应该获取列的别名）
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 获取列值
                    Object columnVal = rs.getObject(i + 1);

                    // 填充对象属性
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnVal);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
