package com.examp.mq.worker;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class ConsumerWoker {

    static final String QUENU_NAME = "my-first-mq-prod";

    public static void main(String[] args) throws IOException {
        //获得一个信道
        Channel channel = RabbitmqUtils.getChannel();

        //消费的回调
        DeliverCallback deliverCallback = (consumerTag, delivery)->{
            System.out.println("消费了消息"+new String(delivery.getBody()));
        };
        //消费被中断
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消费消息被中断");
        };
        System.out.println(Thread.currentThread().getName()+"1等待接收消息...");
        channel.basicConsume(QUENU_NAME,true,deliverCallback,cancelCallback);
    }
}
