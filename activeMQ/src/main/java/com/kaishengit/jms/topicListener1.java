package com.kaishengit.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class topicListener1 implements MessageListener {
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            System.out.println("~~~~~~~>>" + textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
