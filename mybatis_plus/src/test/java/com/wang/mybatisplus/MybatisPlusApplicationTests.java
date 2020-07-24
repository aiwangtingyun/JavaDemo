package com.wang.mybatisplus;

import com.wang.mybatisplus.entity.User;
import com.wang.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList() {
        System.out.println(("----- selectAll method test ------"));

        // UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        // 所以不填写就是无任何条件
        List<User> userList = userMapper.selectList(null);

        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setName("Andy");
        user.setAge(18);
        user.setEmail("123456@163.com");

        int result = userMapper.insert(user);
        System.out.println("insert result : " + result);    // 影响的行数
        System.out.println("user : " + user);               // id自动回填
    }

    @Test
    public void testUpdateByid() {
        User user = new User();
        user.setId(1286448461327196162L);
        user.setAge(24);

        int result = userMapper.updateById(user);
        System.out.println("update result : " + result);
    }

}
