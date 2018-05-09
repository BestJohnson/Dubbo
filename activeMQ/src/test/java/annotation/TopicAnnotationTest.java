package annotation;

import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-jms-topic3.xml")
public class TopicAnnotationTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void creater() throws InterruptedException {
        while (true) {
            Destination destination = new ActiveMQTopic("spring-topic1");
            jmsTemplate.send(destination,new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage("spring-topic1-121  " + new Date().getTime());
                }
            });
            Thread.sleep(2000);
        }
    }



}
