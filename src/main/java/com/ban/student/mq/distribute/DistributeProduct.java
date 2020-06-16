package com.ban.student.mq.distribute;

import com.ban.student.mq.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wangban
 */
public class DistributeProduct {
    private static final String EXCHANGE_NAME = "distribute_exchange";
    private static final String ROUTING_KEY = "distribute_key";
    private static final String QUEUE_NAME = "distribute_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct",  true ,  false ,  null);
        //声明队列
        channel.queueDeclare(QUEUE_NAME ,  true ,  false ,  false ,  null);
        //将交换机和队列绑定起来，通过路由键
        channel.queueBind ( QUEUE_NAME ,  EXCHANGE_NAME , ROUTING_KEY) ;
        for (int a = 0; a < 100; a++) {
            String message ="num:"+ a+";";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.TEXT_PLAIN, message.getBytes());
        }
        System.out.println("结束！");

    }
}
