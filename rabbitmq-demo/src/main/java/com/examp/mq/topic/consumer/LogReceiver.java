package com.examp.mq.topic.consumer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

/**
 * 消费者
 */
public class LogReceiver {

    /*交换机名*/
    private final static String EXCHANGE_NAME = "topic-exchange";

    public static void main(String[] args) throws Exception {
        //获得通道
        Channel channel = RabbitmqUtils.getChannel();
        //声明队列，使用临时队列，断开连接后就被删除
        String queueName = channel.queueDeclare().getQueue();
        //队列和交换机进行绑定，确定路由键
        //队列名，交换机名，路由键
        //路由键----》以rabbit开头的单词
        channel.queueBind(queueName,EXCHANGE_NAME,"rabbit.#");

        //处理消息回调方法
        DeliverCallback deliverCallback = (ConsumerTag,delivery)->{
            System.out.println("消费路由键为"+delivery.getEnvelope().getRoutingKey()+"的消息：");
            System.out.println(new String(delivery.getBody(),"UTF-8"));
        };

        //是否自动应答
        boolean aotuAck = true;
        //消费消息
        channel.basicConsume(queueName,aotuAck,deliverCallback,tag->{});

    }
}
