package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopicPersist {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String TOPIC_NAME = "my-topic";

    @Test
    public void consumer() throws JMSException, IOException {

        System.out.println(">>> 我是订阅者");

        // 创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        // 设置客户ID
        connection.setClientID("wang");

        // 创建会话 session，第一个参数叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 创建主题 topic 目的地
        Topic topic = session.createTopic(TOPIC_NAME);

        // 创建订阅者
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "remark...");

        // 设置完持久化后才可以开启
        connection.start();

        // 接收消息
        Message message = subscriber.receive();
        while (null != message) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("收到持久化消息：" + textMessage.getText());
            message = subscriber.receive(1000L);
        }

        // 关闭资源
        session.close();
        connection.close();
    }
}
