package com.wang.curd;


import com.wang.bean.Customer;
import com.wang.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 针对于Customers表的查询操作
 */
public class CustomerForQuery {

    @Test
    public void testQueryForCustomers() {
        String sql = "select id, name, email, birth from customers where id = ?;";

        System.out.println(queryForCustomers(sql, 1));
        System.out.println(queryForCustomers(sql, 2));
    }

    // 针对 Customers 表的通用查询操作
    public Customer queryForCustomers(String sql, Object... args) {
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
                Customer customer = new Customer();
                int columnCount = rsmd.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列名（为了体现ORM思想应该获取列的别名）
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 获取列值
                    Object columnVal = rs.getObject(i + 1);

                    // 填充对象属性
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(customer, columnVal);
                }
                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }
}
