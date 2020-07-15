package com.wang.curd;

import com.wang.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

/**
 * 使用 PreparedStatement 实现对数据表的增删改操作
 */
public class PreparedStatementUpdate {

    @Test
    public void testUpdate() {
        // 测试更新
        String sql = "update `order` set order_name = ? where order_id = ?;";
        executeUpdate(sql, "CC", 4);
    }

    @Test
    public void testInsert() {
        // 测试插入
        String sql = "insert into `order`(`order_id`, `order_name`, `order_date`) " +
                "values(?, ?, ?);";
        executeUpdate(sql, 5, "DD", new Date(System.currentTimeMillis()));
    }

    @Test
    public void testDelete() {
        // 测试删除
        String sql = "delete from `order` where `order_id` = ?;";
        executeUpdate(sql, 5);
    }

    // 通用的增删改操作
    public void executeUpdate(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 获取 Connection 数据库连接对象
            conn = JDBCUtils.getConnect();

            // 构建 PreparedStatement 对象
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            // 执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            JDBCUtils.closeResource(conn, ps);
        }
    }
}
