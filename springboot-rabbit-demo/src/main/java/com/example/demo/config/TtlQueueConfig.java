package com.example.demo.config;


import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 声明交换机队列和绑定
 */
@Configuration
public class TtlQueueConfig {

    //1.普通交换机 2.死信交换机
    private final static String NORMAL_EXCHANGE = "A",DEAD_LETTER_EXCHANGE= "B";

    //1.普通队列 2.死信队列
    private final static String NORMAL_QUEUE_C = "C",NORMAL_QUEUE_D = "D",DEAD_LETTER_QUEUE = "E";

    @Bean("F")
    public Queue fQueue(){
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key","YD");//延迟时间由生产者控制
        return QueueBuilder.durable("F").withArguments(args).build();
    }
    //绑定到普通交换机
    @Bean
    public Binding fBindingA(@Qualifier("F") Queue fQueue,@Qualifier(NORMAL_EXCHANGE) DirectExchange exchange){
        return BindingBuilder.bind(fQueue).to(exchange).with("FA");
    }

    @Bean(value = NORMAL_EXCHANGE)
    public DirectExchange aExchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }
    @Bean(value = DEAD_LETTER_EXCHANGE)
    public DirectExchange bExchange(){
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }
    @Bean(value = NORMAL_QUEUE_C)
    public Queue cQueue(){
        //绑定死信交换机的参数
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由  key
        arguments.put("x-dead-letter-routing-key","YD");
        //延迟队列的延迟时间----10秒
        arguments.put("x-message-ttl",10000);
        return QueueBuilder
                .durable(NORMAL_QUEUE_C)
                .withArguments(arguments).build();
    }

    @Bean(value = NORMAL_QUEUE_D)
    public Queue dQueue(){
        //绑定死信交换机的参数
        Map<String,Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",30000);//延迟队列的延迟时间----30秒
        return QueueBuilder
                .durable(NORMAL_QUEUE_D)
                .withArguments(arguments).build();
    }
    //死信队列
    @Bean(DEAD_LETTER_QUEUE)
    public Queue eQueue(){
        return new Queue(DEAD_LETTER_QUEUE);
    }
    //绑定路由键
    @Bean
    public Binding aBindingD(
            @Qualifier(NORMAL_EXCHANGE) DirectExchange normalExchange,
            @Qualifier(NORMAL_QUEUE_D) Queue normalQueueD
    ){
        return BindingBuilder.bind(normalQueueD).to(normalExchange).with("DA");
    }
    @Bean
    public Binding aBindingC(
            @Qualifier(NORMAL_EXCHANGE) DirectExchange normalExchange,
            @Qualifier(NORMAL_QUEUE_C) Queue normalQueueC
    ){
        return BindingBuilder.bind(normalQueueC).to(normalExchange).with("CA");
    }
    //绑定死信队列
    @Bean
    public Binding bBindingE(
            @Qualifier(DEAD_LETTER_EXCHANGE) DirectExchange DeadLetterExchange,
            @Qualifier(DEAD_LETTER_QUEUE) Queue normalQueueE
    ){
        return BindingBuilder.bind(normalQueueE).to(DeadLetterExchange).with("YD");
    }


}
