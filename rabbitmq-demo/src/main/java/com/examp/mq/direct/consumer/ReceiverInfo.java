package com.examp.mq.direct.consumer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.impl.AMQImpl;

import java.nio.charset.StandardCharsets;

public class ReceiverInfo {

    private static final String EXCHANGE_NAME = "log-exchange";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqUtils.getChannel();
        //直接类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //
        channel.queueDeclare("console",false,false,false,null);

        channel.queueBind("console",EXCHANGE_NAME,"info");

        DeliverCallback callback = (consumerTag,delivery)->{
            System.out.println("消费消息======>"+new String(delivery.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume("console",callback,(consumerTag,delivery)->{});

    }
}
