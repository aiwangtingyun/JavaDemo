package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerRedelivery {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "my-queue";

    public static void main(String[] args) throws JMSException, IOException {

        // 创建连接工厂，按照指定的URL地址，采用默认用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 创建消息重投策略
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setRedeliveryDelay(1500);    // 重投时间间隔
        policy.setMaximumRedeliveries(3);   // 重投次数
        activeMQConnectionFactory.setRedeliveryPolicy(policy);

        // 连接连接工厂获得 connect 连接池并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        // 创建会话 session 并开启事务
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

        // 创建队列目的地
        Queue queue = session.createQueue(QUEUE_NAME);

        // 创建消息的消费者
        MessageConsumer messageConsumer = session.createConsumer(queue);

        // 从 MQ 队列中获取消息
        while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(2000L);
            if (textMessage != null) {
                System.out.println("消费者接收到的消息 ====> " + textMessage.getText());
            } else {
                break;
            }
        }
        // 不提交事务导致重复消费
        // session.commit();

        // 7、关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
