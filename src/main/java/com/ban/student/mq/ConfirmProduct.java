package com.ban.student.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @author wangban
 */
public class ConfirmProduct {
    private static final String EXCHANGE_NAME = "confirm_demo";
    private static final String ROUTING_KEY = "confirm_routingkey_demo";
    private static final String QUEUE_NAME = "confirm_queue";
    private static final int BATCH_COUNT = 10000;
    private static final SortedSet<Long> set = new TreeSet<>();

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        //声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        //发送方确认模式
        channel.confirmSelect();
        System.out.println("程序开始时间：" + System.currentTimeMillis());
//        //普通
        normalConfirm(channel);
//        //批量
        batchConfirm(channel);
        //异步
        asynConfirm(channel);

        System.out.println("程序执行完成：" + System.currentTimeMillis());
    }

    private static void asynConfirm(Channel channel) throws IOException, TimeoutException, InterruptedException {
        long start = System.currentTimeMillis();
        for (int a = 0; a < BATCH_COUNT; a++) {
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            set.add(nextPublishSeqNo);
            String message = "confirm message！";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.TEXT_PLAIN, message.getBytes());
        }
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck");
                if(multiple){
                    set.headSet(deliveryTag-1).clear();
                }else{
                    set.remove(deliveryTag);
                }
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleNack");
                if(multiple){
                    set.headSet(deliveryTag-1).clear();
                }else{
                    set.remove(deliveryTag);
                }
            }
        });
//        while(true){
//            if(set.size()==0){
//                break;
//            }
//        }
        System.out.println("异步结束时间：" + Math.subtractExact(System.currentTimeMillis(), start));

    }

    private static void batchConfirm(Channel channel) throws IOException, TimeoutException, InterruptedException {
        long start = System.currentTimeMillis();
        for (int a = 0; a < BATCH_COUNT; a++) {
            String message = "confirm message！";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.TEXT_PLAIN, message.getBytes());
        }
        if (channel.waitForConfirms()) {
            System.out.println("批量发送成功！");
        }
        System.out.println("普通结束时间：" + Math.subtractExact(System.currentTimeMillis(), start));

    }

    private static void normalConfirm(Channel channel) throws IOException, TimeoutException, InterruptedException {
        long start = System.currentTimeMillis();
        for (int a = 0; a < BATCH_COUNT; a++) {
            String message = "confirm message！";
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.TEXT_PLAIN, message.getBytes());
            if (channel.waitForConfirms()) {
                System.out.println("普通发送成功");
            }
        }
        System.out.println("普通结束时间：" + Math.subtractExact(System.currentTimeMillis(), start));

    }
}
