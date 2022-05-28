package com.examp.mq.topic.producer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 消息生产者
 */
public class Emitlog {

    /*交换机名*/
    private final static String EXCHANGE_NAME = "topic-exchange";

    public static void main(String[] args) throws Exception{
        //获得通道
        Channel channel = RabbitmqUtils.getChannel();
        //声明交换机
        //使用主题模式-----》更加灵活
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        System.out.println("==========等待生产者发生消息==========");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext())
        {
            //输入消息
            String msg = scanner.next();
            //发生消息到交换机
            channel.basicPublish(EXCHANGE_NAME,"rabbit.a.b.c",null,msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
