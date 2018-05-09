package spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.IOException;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-jms-topic2.xml")
public class SpringJmsTopicTest {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Test
    public void creater() throws InterruptedException {
        while (true) {
            jmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createTextMessage("spring-topic-111" + new Date().getTime());
                }
            });
            Thread.sleep(2000);
        }
    }

    @Test
    public void consumer() throws IOException {
        System.out.println("here");
        System.in.read();
    }


}
