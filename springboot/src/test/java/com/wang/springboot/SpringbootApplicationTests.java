package com.wang.springboot;

import com.wang.springboot.bean.Person;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

//@ImportResource(locations = {"classpath:beans.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    Person person;

    @Autowired
    ApplicationContext ioc;

    @Test
    void testHelloService() {
        System.out.println("contain helloService Bean ==== > " + ioc.containsBean("helloService"));
    }

    @Test
    void contextLoads() {
        System.out.println(person);
    }

    Logger logger = LoggerFactory.getLogger(getClass());
    @Test
    public void testLoggin() {
        logger.trace("这是trace日志...");
        logger.debug("这是debug日志...");
        logger.info("这是info日志...");
        logger.warn("这是warn日志...");
        logger.error("这是error日志...");
    }

}
