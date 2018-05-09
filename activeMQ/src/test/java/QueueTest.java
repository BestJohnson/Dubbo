import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.camel.Produce;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class QueueTest {

    @Test
    public void send() throws JMSException, IOException {

        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            //1、连接工厂
            //ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
            //设置重试次数
            redeliveryPolicy.setMaximumRedeliveries(3);
            //初次重试延迟时间，毫秒
            redeliveryPolicy.setInitialRedeliveryDelay(3000);
            //每次重试的延迟时间
            redeliveryPolicy.setRedeliveryDelay(3000);

            connectionFactory.setRedeliveryPolicy(redeliveryPolicy);

            //2、创建连接，开启
            connection = connectionFactory.createConnection();
            connection.start();
            //3、创建session
            session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
            //4、消息目的地
            Destination destination = session.createQueue("qq-queue");
            //5、消息生产者
            producer = session.createProducer(destination);
            //持久模式
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //6、发送
            TextMessage message = session.createTextMessage("message-3");
            producer.send(message);

            //提交事务
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();


        } finally {
            //7、释放资源
            close(session,connection,producer);
        }


    }

    @Test
    public void consumerMessage() throws JMSException, IOException {
        //1、连接工厂
        //ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //设置重试次数
        redeliveryPolicy.setMaximumRedeliveries(3);
        //初次重试延迟时间，毫秒
        redeliveryPolicy.setInitialRedeliveryDelay(3000);
        //每次重试的延迟时间
        redeliveryPolicy.setRedeliveryDelay(3000);

        connectionFactory.setRedeliveryPolicy(redeliveryPolicy);


        //2、创建连接，开启
        Connection connection = connectionFactory.createConnection();
        connection.start();
        //3、创建session true/false表示使用事务与否
        final Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
        //4、消息目的地
        Destination destination = session.createQueue("qq-queue");

        //5、创建消费者
        MessageConsumer consumer = session.createConsumer(destination);
        //6、获取消息
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;

                try {
                    String text = textMessage.getText();
                    System.out.println(text);

                    if(text.equals("message-3")) {
                        throw new RuntimeException("手动抛的异常");
                    }
                    //手动签收
                    textMessage.acknowledge();

                    //自动签收模式下，不catch异常，触发重试
                    /*if(text.equals("message-3")) {
                        throw new RuntimeException("手动抛的异常");
                    }*/

                } catch (Exception e){
                    e.printStackTrace();
                    try {
                        session.recover();
                    } catch (JMSException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        System.in.read();

        //7、释放资源
        consumer.close();
        session.close();
        connection.close();
    }























    private void close(Session session,Connection connection,MessageProducer producer) {
        if(producer != null) {
            try {
                producer.close();
            } catch (JMSException e) {
                e.printStackTrace();
            } finally {
                if(session != null) {
                    try {
                        session.close();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    } finally {
                        if(connection != null) {
                            try {
                                connection.close();
                            } catch (JMSException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

    }

}
