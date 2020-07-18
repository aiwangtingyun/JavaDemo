package com.wang.boot_activemq;

import com.wang.boot_activemq.producer.QueueProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@SpringBootTest(classes = BootActivemqApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class BootActivemqApplicationTests {

    @Resource
    private QueueProducer queueProducer;

    // 测试发送消息
    @Test
    public void textSend() throws Exception {
        queueProducer.produceMsg();
    }

}
