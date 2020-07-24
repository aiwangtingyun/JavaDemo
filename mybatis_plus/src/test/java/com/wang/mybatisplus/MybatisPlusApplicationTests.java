package com.wang.mybatisplus;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wang.mybatisplus.entity.User;
import com.wang.mybatisplus.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    // 查询所有数据
    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));

        // UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        // 所以不填写就是无任何条件
        List<User> userList = userMapper.selectList(null);

        userList.forEach(System.out::println);
    }

    // 插入一条数据
    @Test
    public void testInsert() {
        User user = new User();
        user.setName("Lily");
        user.setAge(16);
        user.setEmail("123456@163.com");

        int result = userMapper.insert(user);
        System.out.println("insert result : " + result);    // 影响的行数
        System.out.println("user : " + user);               // id自动回填
    }

    // 根据ID更新数据
    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(6L);
        user.setAge(24);

        int result = userMapper.updateById(user);
        System.out.println("update result : " + result);
    }

    /**
     * 测试乐观锁插件
     */
    @Test
    public void testOptimisticLocker() {
        // 查询
        User user = userMapper.selectById(1L);

        // 修改数据
        user.setName("wang");
        user.setAge(22);

        // 执行更新
        userMapper.updateById(user);
    }

    /**
     * 模拟乐观锁修改失败
     */
    @Test
    public void testOptimisticLockFailed() {
        // 查询
        User user = userMapper.selectById(1L);

        // 修改数据
        user.setName("zhang");
        user.setAge(24);

        // 模拟取出数据后，数据库中version实际数据比取出的值大，即已被其它线程修改并更新了 version
        user.setVersion(user.getVersion() - 1);

        // 执行更新
        userMapper.updateById(user);
    }

    // 根据ID查询
    @Test
    public void testSelectById() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    // 通过多个ID批量查询
    @Test
    public void testSelectBatchIds() {
        List<User> userList = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        userList.forEach(System.out::println);
    }

    // 简单的条件查询
    @Test
    public void testSelectByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "wang");
        map.put("age", 22);

        List<User> userList = userMapper.selectByMap(map);
        userList.forEach(System.out::println);
    }

    // 测试 selectPage 分页
    @Test
    public void testSelectPage() {
        Page<User> page = new Page<>(1, 5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    // 测试 selectMapsPage 分页
    @Test
    public void testSelectMapsPage() {
        Page<User> page = new Page<>(1, 5);
        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, null);

        // 注意：此行必须使用 mapIPage 获取记录列表，否则会有数据类型转换错误
        mapIPage.getRecords().forEach(System.out::println);

        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    // 根据ID删除记录
    @Test
    public void testDeleteById() {
        int result = userMapper.deleteById(7L);
        System.out.println(result);
    }

    // 批量删除
    @Test
    public void testDeleteBatchIds() {
        int result = userMapper.deleteBatchIds(Arrays.asList(4, 5, 6));
        System.out.println(result);
    }

    // 根据简单条件查询删除
    @Test
    public void testDeleteByMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Tom");
        map.put("age", 28);

        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }
}
