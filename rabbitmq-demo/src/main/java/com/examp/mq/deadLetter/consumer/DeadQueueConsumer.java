package com.examp.mq.deadLetter.consumer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class DeadQueueConsumer {


    static final String DEAD_QUEUE="dead_q";

    public static void main(String[] args) throws Exception{
        //声明通道
        Channel channel = RabbitmqUtils.getChannel();

        System.out.println("==========死信队列等待消费消息==========");
        //消费回调函数
        DeliverCallback deliverCallback = (tag, msg)->{
            System.out.println(new String(msg.getBody(),"UTF-8"));

        };
        //消费信息
        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,tag->{});
    }
}
