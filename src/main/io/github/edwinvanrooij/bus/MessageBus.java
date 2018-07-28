package io.github.edwinvanrooij.bus;

import com.rabbitmq.client.*;
import io.github.edwinvanrooij.Const;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static io.github.edwinvanrooij.Util.log;

public class MessageBus {

    private String exchangeName;

    public MessageBus(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void produceMessage(String message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Const.IP);

//            factory.setUsername("guest");
//            factory.setPassword("guest");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);

            channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");

            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void consumeMessage(MessageHandler handler, String queueName) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Const.IP);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
            channel.queueDeclare(queueName, false, false, false, null);
//            String queueName = channel.queueDeclare().getQueue();
//            String queueName = session.getId();
            channel.queueBind(queueName, exchangeName, "");

            System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    handler.handleMessage(message);
                }
            };
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
