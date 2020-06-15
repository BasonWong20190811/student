package com.ban.student.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangban
 */
public class MQProduce {
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

        createDLX(channel);

        //DLX

    }

    private static void createDLX(Channel channel) throws Exception {
        //创建DLX
        channel.exchangeDeclare("exchange.dlx", "direct", true);

        channel.exchangeDeclare("exchange.normal","fanout",true);

        Map<String,Object> map = new HashMap<>();
//        //设置过期时间
//        map.put("x-message-ttl",10);
        //添加DLX
        map.put("x-dead-letter-exchange","exchange.dlx");
        //设置DLX的路由键
        map.put("x-dead-letter-routing-key", "routingkey");
        channel.queueDeclare("queue.normal", true, false, false, map);
        channel.queueBind("queue.normal", "exchange.normal", "rk");
        channel.queueDeclare("queue.dlx", true, false, false, null);
        channel.queueBind("queue.dlx", "exchange.dlx", "routingkey");
        channel.basicPublish("exchange.normal", "rk", MessageProperties.PERSISTENT_TEXT_PLAIN,"ssss".getBytes());

    }
}
