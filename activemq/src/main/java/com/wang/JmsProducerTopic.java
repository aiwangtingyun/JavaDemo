package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class JmsProducerTopic {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String TOPIC_NAME = "my-topic";

    @Test
    public void producer() throws JMSException {
        // 1、创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话 session，第一个参数叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建目的地为主题 topic
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5、创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);

        // 6、通过生产者生产消息到 MQ 的队列里面
        for (int i = 0; i < 6; i++) {
            // 7、创建指定格式的消息
            TextMessage textMessage = session.createTextMessage("message ====> " + i);
            // 8、通过 messageProducer 发送消息给 mq
            messageProducer.send(textMessage);
        }

        // 9、关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("======== 消息发布到 MQ 完成 ==========");
    }
}
