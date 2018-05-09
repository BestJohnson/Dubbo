package com.kaishengit.jms.annotation;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
@Component
public class annotationQueueListener {

    @JmsListener(destination = "spring-queue")
    public void queueConsumer(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            System.out.println("queue使用注解" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    @JmsListener(destination = "spring-topic1",containerFactory = "containerFactory")
    public void topicConsumer(Message message) {
        TextMessage textMessage = (TextMessage) message;

        try {
            System.out.println("topic使用注解" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
