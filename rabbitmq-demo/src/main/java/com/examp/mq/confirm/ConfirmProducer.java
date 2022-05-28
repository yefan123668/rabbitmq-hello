package com.examp.mq.confirm;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmProducer {

    public static void main(String[] args) throws IOException, InterruptedException {
        confirmAsync();
    }

    public static void selectConfirm() throws IOException, InterruptedException {
        Channel channel = RabbitmqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化   默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费   是否进行共享   true 可以多个消费者消费
         * 4.是否自动删除   最后一个消费者端开连接以后   该队列是否自动删除   true 自动删除
         * 5.其他参数
         */
        boolean durable = true;
        channel.queueDeclare("Confirm_Queue",durable,false,true,null);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String msg = i+"";
            channel.basicPublish("","Confirm_Queue", MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8));
            //服务端返回  false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布消息耗时"+(end-begin));

    }

    public static void confirmAsync() throws IOException {
        Channel channel = RabbitmqUtils.getChannel();
        //开启发布确认
        channel.confirmSelect();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列里面的消息是否持久化   默认消息存储在内存中
         * 3.该队列是否只供一个消费者进行消费   是否进行共享   true 可以多个消费者消费
         * 4.是否自动删除   最后一个消费者端开连接以后   该队列是否自动删除   true 自动删除
         * 5.其他参数
         */
        boolean durable = true;
        channel.queueDeclare("Confirm_async_Queue",durable,false,true,null);

        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况
         * 1.轻松的将序号与消息进行关联
         * 2.轻松批量删除条目    只要给到序列号
         * 3.支持并发访问
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirms  = new ConcurrentSkipListMap<>();

        /**
         * 确认收到消息的一个回调
         * 1.消息序列号
         * 2.true 可以确认小于等于当前序列号的消息
         *   false 确认当前序列号消息
         */
        ConfirmCallback successConfirm = (sequenceNumber, multiple)->{
            //删除已经确认的消息
            if (multiple){  //如果是批量的
                //返回的是小于等于当前序列号的却认消息    是一个  map
                ConcurrentNavigableMap<Long,String> confirmed =
                        outstandingConfirms.headMap(sequenceNumber);
                //清除该部分未确认消息
                confirmed.clear();
            } else {  //不是批量的
                //只清除当前序列号的消息
                outstandingConfirms.remove(sequenceNumber);
            }
            System.out.println("确认的消息"+sequenceNumber);
        };

        ConfirmCallback unConfirm = (sequenceNumber, multiple)->{
            String s = outstandingConfirms.get(sequenceNumber);
            System.out.println("未确认的消息"+s);
        };

        //监听回调方法，它是另一个线程  相当于开启了一个分支
        channel.addConfirmListener(successConfirm,unConfirm);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String msg = i+"";
//            channel.basicPublish("","async_confirm",MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes(StandardCharsets.UTF_8));
            outstandingConfirms.put(channel.getNextPublishSeqNo(), msg);

            channel.basicPublish("", "Confirm_async_Queue",MessageProperties.PERSISTENT_TEXT_PLAIN,
                    msg.getBytes());
        }
        long end  = System.currentTimeMillis();
        System.out.println("耗时"+(end-begin)+"ms");

    }
}
