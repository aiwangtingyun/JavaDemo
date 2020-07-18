package com.wang.boot_activemq.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.UUID;

@Component
public class QueueProducer {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    // 发送消息
    public void produceMsg() {
        jmsMessagingTemplate.convertAndSend(queue, "boot message: " + UUID.randomUUID().toString().substring(0,6));
    }

    // 每间隔3秒发一条消息
    @Scheduled(fixedDelay = 3000)
    public void scheduleProduceMsg() {
        jmsMessagingTemplate.convertAndSend(queue, "boot message: " + UUID.randomUUID().toString().substring(0,6));
        System.out.println("发送 boot 消息成功！");
    }
}
