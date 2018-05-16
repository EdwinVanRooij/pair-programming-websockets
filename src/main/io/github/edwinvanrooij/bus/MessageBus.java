package io.github.edwinvanrooij.bus;

import com.rabbitmq.client.*;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static io.github.edwinvanrooij.Util.log;

public class MessageBus {

    public static String QUEUE_NAME = "dpi_pair_programming_queue";

    private Session session;

    public MessageBus(Session session) {
        this.session = session;
    }

    public void produceMessage(String json) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, json.getBytes());
            log("Sent '%s' to queue.", json);

            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public void consumeMessage(MessageHandler handler) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            log("Waiting for messages. To exit press CTRL+C");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");
                    handler.handleMessage(session, message);
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
