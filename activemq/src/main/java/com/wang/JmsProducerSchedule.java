package com.wang;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;
import org.apache.activemq.ScheduledMessage;
import org.junit.Test;

import javax.jms.*;
import java.util.UUID;

public class JmsProducerSchedule {

    public static final String ACTIVEMQ_URL = "tcp://localhost:61616";
    public static final String QUEUE_NAME = "my-queue";

    public static void main(String[] args)  throws JMSException {

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageProducer messageProducer = session.createProducer(queue);

        // 设置时间
        long delay = 3 * 1000;  // 延时3秒
        long period = 4 * 1000; // 定时4秒
        int repeat = 5;         // 重复5次

        for (int i = 0; i < 3; i++) {
            TextMessage message = session.createTextMessage("schedule message " + i);

            // 设置消息 Schedule 参数
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
            message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);

            messageProducer.send(message);
        }

        messageProducer.close();
        session.close();
        connection.close();

        System.out.println("======== 消息发布到 MQ 完成 ==========");
    }
}
