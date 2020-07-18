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
public class SpringMQProducer {

    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    @Test
    public void producer() {
        // 获取 jmsTemplate 对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        SpringMQProducer producer = (SpringMQProducer) context.getBean("springMQProducer");
        JmsTemplate jmsTemplate = producer.getJmsTemplate();

        // 发送消息
        // 使用匿名内部类方式
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage message = session.createTextMessage("Spring ActiveMq Message....");
                return message;
            }
        });
        // 使用 lambda 表达式
        jmsTemplate.send((session) -> {
            TextMessage message = session.createTextMessage("Spring ActiveMq Message....");
            return message;
        });
    }
}
