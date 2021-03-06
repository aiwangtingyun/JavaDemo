package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "my-queue";

    public static void main(String[] args) throws JMSException, IOException {

        System.out.println(">>>>> 我是消费者");

        // 1、创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话 session，第一个参数叫事务，第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // 4、创建目的地：队列还是主题 topic
        Queue queue = session.createQueue(QUEUE_NAME);

        // 5、创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        // 6、从 MQ 队列中获取消息
        // 通过监听方式来获取消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("消费者接收到的消息 : textMessage ---> " + textMessage.getText());
                        System.out.println("Message type : " + textMessage.getStringProperty("type"));
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
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
