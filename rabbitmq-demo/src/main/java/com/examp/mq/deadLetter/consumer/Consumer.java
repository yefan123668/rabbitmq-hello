package com.examp.mq.deadLetter.consumer;

import com.examp.mq.utils.RabbitmqUtils;
import com.examp.mq.utils.SleepUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer {

    /*普通队列*/
    static final String NORMAL_QUEUE="normal_q";
    /*死信队列*/
    static final String DEAD_QUEUE="dead_q";
    //用于声明普通交换机和私信交换机
    static final String NORMAL_EXCHANGE="normal_e";
    static final String DEAD_EXCHANGE="dead_e";

    public static void main(String[] args) throws Exception{
        //声明通道
        Channel channel = RabbitmqUtils.getChannel();
        //声明普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明死信交换机
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"routing-key-lisi");

        //声明普通队列
        //需要在参数里绑定死信交换机
        // 需要通过队列里的参数去绑定参数----队列里的的消息发生某些情况不会被删除，会转发到死信交换机
        Map<String,Object> argument = new HashMap<>();
        argument.put("x-dead-letter-routing-key","routing-key-lisi");
        argument.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        argument.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,argument);
        //绑定普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"routing-key-wangwu");

        System.out.println("==========普通队列等待消费消息==========");
        //消费回调函数
        DeliverCallback deliverCallback = (tag,msg)->{
            if(new String(msg.getBody()).contains("5")){SleepUtils.sleep(19);}
            System.out.println(new String(msg.getBody(),"UTF-8"));

        };
        //消费信息
        channel.basicConsume(NORMAL_QUEUE,true,deliverCallback,tag->{});
    }
}
