package com.wang.activemq;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Service
public class SpringMQConsumer {

    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    @Test
    public void consumer() {
        // 获取 jmsTemplate 对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringMQConsumer consumer = (SpringMQConsumer) context.getBean("springMQConsumer");
        JmsTemplate jmsTemplate = consumer.getJmsTemplate();

        // 接收消息
        String message = (String) jmsTemplate.receiveAndConvert();

        System.out.println("接收的消息为：" + message);
    }
}
