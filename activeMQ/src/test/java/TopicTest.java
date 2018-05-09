import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class TopicTest {

    @Test
    public void creatTopic() throws JMSException {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(true,Session.CLIENT_ACKNOWLEDGE);

        Topic topic = session.createTopic("hello-topic");

        MessageProducer producer = session.createProducer(topic);

        TextMessage textMessage = session.createTextMessage("topic-1");
        producer.send(textMessage);

        session.commit();

        producer.close();
        session.close();
        connection.close();

    }

    @Test
    public void consumerTopic() throws JMSException, IOException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        Connection connection = connectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(true,Session.CLIENT_ACKNOWLEDGE);

        Topic topic = session.createTopic("hello-topic");

        MessageConsumer consumer = session.createConsumer(topic);

        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println(textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }

        });

        System.in.read();

        consumer.close();
        session.close();
        connection.close();

    }




}
