package com.ban.student.mq;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author wangban
 */
public class RPCServer {
    private static final String RPC_QUEUE_NAME = "rpc_queue";
    private static final String IP_ADDRESS = "192.168.91.129";
    private static final int PORT = 5672;

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //
        channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        System.out.println("[X] Awaiting RPC request");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties().builder().correlationId(properties.getCorrelationId()).build();
                String response = "";
                try {
                    String message = new String(body);
                    int n = Integer.parseInt(message);
                    response += fib(n);

                } catch (Exception e) {
                    System.out.println("[X] error:"+e.getMessage());
                } finally {
                    channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes());
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(RPC_QUEUE_NAME, false,consumer);

    }

    private static int fib(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n-1) + fib(n-2);
    }
}
