package com.kaishengit.jms;

import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
public class queueListener3 implements SessionAwareMessageListener {


    public void onMessage(Message message, Session session) throws JMSException {
        try {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("重试机制" + textMessage.getText());

            if (1 == 1) {
                throw new RuntimeException("手动抛出异常");
            }
            textMessage.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
            session.recover();
        }
    }
}
