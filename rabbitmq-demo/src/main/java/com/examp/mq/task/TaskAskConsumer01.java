package com.examp.mq.task;

import com.examp.mq.utils.RabbitmqUtils;
import com.examp.mq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;

public class TaskAskConsumer01 {

    final static String QUEUE_NAME = "my-first-mq-prod";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.getChannel();

        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化   默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费   是否进行共享   true 可以多个消费者消费
         * 4.是否自动删除   最后一个消费者端开连接以后   该队列是否自动删除   true 自动删除
         * 5.其他参数
         */
        boolean durable = false;
        channel.queueDeclare(QUEUE_NAME,durable,false,false,null);

        System.out.println("C等待接收消息。。。");
        //设置不要批量处理。手动应答确认。
        DeliverCallback deliverCallback = (consumerTag,delivery)->{
            String msg = new String(delivery.getBody());
            System.out.println("开始消费了消息，"+msg);
            SleepUtils.sleep(30);
            //消息标记tag,不用批量处理
            System.out.println("处理了一个消息...");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        //手动应答
        Boolean aotuAck = false;
        channel.basicConsume(QUEUE_NAME,aotuAck,deliverCallback,consumer->{});
    }
}
