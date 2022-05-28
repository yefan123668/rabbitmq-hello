package com.examp.mq.deadLetter.producer;

import com.examp.mq.utils.RabbitmqUtils;
import com.examp.mq.utils.SleepUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * 死信队列的生产者
 */
public class Producer {


    static final String NORMAL_EXCHANGE="normal_e";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitmqUtils.getChannel();

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                //设置过期时间为9秒
                .expiration("90000").build();

        System.out.println("==========生产者开始发消息==========");
        //发生11条数据
        for (int i = 0; i < 11; i++) {
            channel.basicPublish(NORMAL_EXCHANGE,"routing-key-wangwu",properties,("第"+i+"条消息").getBytes(StandardCharsets.UTF_8));

            System.out.println("生产者开始发消息"+i);
        }

    }
}
