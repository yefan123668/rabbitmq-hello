package com.examp.mq.fanout.consumer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiverLog1 {

    static final String EXCHANGE_NAME = "my-exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtils.getChannel();
        //声明一个交换机  名称和类型
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列  临时队列  名称随机  当消费者断开和它的链接时，它会被删除
        String queue = channel.queueDeclare().getQueue();
        //队列绑定交换机  第三个是RoutingBind
        channel.queueBind(queue,EXCHANGE_NAME,"");

        System.out.println("消费在等待消费消息...");
        DeliverCallback callback = (consumerTag,delivery)->{
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("数据"+msg+"写入成功");
            //file
        };
        boolean aotuAck = false;
        channel.basicConsume(queue,aotuAck,callback,(consumerTag,delivery)->{});
    }
}
