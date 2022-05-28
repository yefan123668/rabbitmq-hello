package com.example.demo.config;

import com.example.demo.common.constants.AMQPConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ConfirmConfig {

    /**
     * 声明一个队列
     */
    @Bean
    public Queue myQueue(){
        return QueueBuilder.durable(AMQPConstants.CONFIRM_QUEUE).build();
    }
    /*声明一个消息队列*/
    @Bean
    public DirectExchange myDirect(){
        return ExchangeBuilder.directExchange(AMQPConstants.CONFIRM_EXCHANGE).durable(true)
                //添加它的备份交换机-----解决不可路由的消息不用回退给生产者
                .withArgument("alternate-exchange",AMQPConstants.BACKUP_EXCHANGE).build();
        //return new DirectExchange(AMQPConstants.CONFIRM_EXCHANGE,true,false);
    }
    /*声明一个队列绑定*/
    @Bean
    public Binding myBind(
            @Qualifier("myQueue") Queue queue,
            @Qualifier("myDirect") DirectExchange exchange
    ){
        return BindingBuilder.bind(queue).to(exchange).with(AMQPConstants.CONFIRM_ROUTING_KEY);
    }

    /*备份交换机和队列，还有报警队列*/
    @Bean
    public FanoutExchange backupExchange(){
        return ExchangeBuilder.fanoutExchange(AMQPConstants.BACKUP_EXCHANGE).build();
    }

    @Bean
    public Queue backQueue(){
        return QueueBuilder.durable(AMQPConstants.BACKUP_QUEUE).build();
    }

    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(AMQPConstants.BACKUP_QUEUE_WARNING).build();
    }

    @Bean
    public Binding queueBindBackEx(
            @Qualifier("backupExchange") FanoutExchange exchange,
            @Qualifier("backQueue") Queue backQueue
            ){
        System.out.println(111);
        return BindingBuilder.bind(backQueue).to(exchange);
    }
    @Bean
    public Binding warningQueueBindBackEx(
            @Qualifier("backupExchange") FanoutExchange exchange,
            @Qualifier("warningQueue") Queue warningQueue
    ){
        return BindingBuilder.bind(warningQueue).to(exchange);
    }

}
