package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;
import org.junit.Test;

import javax.jms.*;
import java.util.UUID;

public class JmsProducerAsync {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "my-queue";

    @Test
    public void producer() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        activeMQConnectionFactory.setUseAsyncSend(true);    // 设置异步发送

        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);

        // 注意，需要使用的是 ActiveMQMessageProducer
        ActiveMQMessageProducer activeMQMessageProducer = (ActiveMQMessageProducer) session.createProducer(queue);
        activeMQMessageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

        TextMessage textMessage = null;
        for (int i = 0; i < 6; i++) {
            textMessage = session.createTextMessage("message " + i);

            // 使用 ID 标识消息
            textMessage.setJMSMessageID("message id : " + UUID.randomUUID().toString());
            String messageID = textMessage.getJMSMessageID();

            // 设置异步发送回调
            activeMQMessageProducer.send(textMessage, new AsyncCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("异步消息发送成功 " + messageID);
                }

                @Override
                public void onException(JMSException exception) {
                    // 异步发送失败后的处理
                    System.out.println("异步消息发送失败 " + messageID);
                }
            });

        }

        activeMQMessageProducer.close();
        session.close();
        connection.close();

        System.out.println("======== 消息发布到 MQ 完成 ==========");
    }
}
