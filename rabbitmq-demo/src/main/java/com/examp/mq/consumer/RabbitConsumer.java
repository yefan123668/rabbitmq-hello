package com.examp.mq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者消费消息
 */
public class RabbitConsumer {

    private  static String  QUENE_NAME = "my-first-mq-prod";

    public static void main(String[] args) throws IOException, TimeoutException {
        //连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123");
        //设置IP
        factory.setHost("43.142.26.233");
        //获得一个信道,自动close
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //消费的回调
        DeliverCallback deliverCallback = (consumerTag,delivery)->{
            System.out.println("消费了消息"+new String(delivery.getBody()));
        };
        //消费被中断
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println("消费消息被中断");
        };
        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答   true 代表自动应答   false 手动应答
         * 3.消费者未成功消费的回调
         */
        channel.basicConsume(QUENE_NAME,deliverCallback,cancelCallback);
        System.out.println("消费了一个消息...");
    }
}
