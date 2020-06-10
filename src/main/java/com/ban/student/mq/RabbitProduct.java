package com.ban.student.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wangban
 */
public class RabbitProduct {
    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "192.168.91.129";
    private static final int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct",  true ,  false ,  null);
        //声明队列
        channel.queueDeclare(QUEUE_NAME ,  true ,  false ,  false ,  null);
        //将交换机和队列绑定起来，通过路由键
        channel.queueBind ( QUEUE_NAME ,  EXCHANGE_NAME , ROUTING_KEY) ;
        String message="hello world";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY, MessageProperties.TEXT_PLAIN,message.getBytes());
        channel.close();
        connection.close();


    }


}
