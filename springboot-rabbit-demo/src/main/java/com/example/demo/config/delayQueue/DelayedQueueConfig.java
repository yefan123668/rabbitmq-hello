package com.example.demo.config.delayQueue;

import com.example.demo.common.constants.AMQPConstants;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class DelayedQueueConfig {

    @Bean
    public Queue queue(){
        return new Queue(AMQPConstants.DELAY_QUEUE);
    }

    @Bean
    public CustomExchange customExchange(){
        Map<String, Object> args = new HashMap<>();
        //声明延迟的类型
        args.put("x-delayed-type","direct");
        return new CustomExchange(AMQPConstants.DELAY_EXCHANGE,
                //交换机类型
                "x-delayed-message",true,false,
                args);
    }

    @Bean
    public Binding qBindingEx(
            @Qualifier("queue") Queue queue,
            @Qualifier("customExchange") CustomExchange exchange
    ){

        return BindingBuilder.bind(queue).to(exchange).with(AMQPConstants.DELAY_ROUTING_KEY).noargs();
    }
}
