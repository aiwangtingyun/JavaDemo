package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopic {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String TOPIC_NAME = "wang-topic";

    @Test
    public void consumer1() throws JMSException, IOException {

        System.out.println(">>> 我是1号消费者");

        // 1、创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话 session，第一个参数叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建目的地：队列还是主题 topic
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5、创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        // 6、从 MQ 队列中获取消息
        // 使用同步阻塞方法：receive() 会在接收到消息之前或超时之前一直阻塞
        while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(3000L);
            if (textMessage != null) {
                System.out.println("消费者接收到的消息 ====> " + textMessage.getText());
            } else {
                break;
            }
        }

        // 7、关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void consumer2() throws JMSException, IOException {

        System.out.println(">>>>> 我是2号消费者");

        // 1、创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话 session，第一个参数叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建目的地：队列还是主题 topic
        Topic topic = session.createTopic(TOPIC_NAME);

        // 5、创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        // 6、从 MQ 队列中获取消息
        // 通过监听方式来获取消息
        messageConsumer.setMessageListener((message) -> {
            if (null != message && message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("消费者接收到的消息 ====> " + textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();   // 阻塞等待

        // 7、关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
