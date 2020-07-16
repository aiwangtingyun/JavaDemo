package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTx {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "queue01";

    @Test
    public void consumer() throws JMSException, IOException {

        System.out.println(">>> 我是1号消费者");

        // 1、创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2、连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 3、创建会话 session，第一个参数叫事务，第二个叫签收
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

        // 4、创建目的地：队列还是主题 topic
        Queue queue = session.createQueue(QUEUE_NAME);

        // 5、创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        // 6、从 MQ 队列中获取消息
        // 使用同步阻塞方法：receive() 会在接收到消息之前或超时之前一直阻塞
        while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(3000L);
            if (textMessage != null) {
                System.out.println("消费者接收到的消息 ====> " + textMessage.getText());
                // 签收消息
                textMessage.acknowledge();
            } else {
                break;
            }
        }
        // 开启事务后，消费消息必须提交事务，否则会出现重复消费消息问题
        session.commit();

        // 7、关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
