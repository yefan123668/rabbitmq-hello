package com.example.demo.consumer;

import com.example.demo.common.constants.AMQPConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfirmConsumer {

    @RabbitListener(queues = AMQPConstants.CONFIRM_QUEUE)
    public void consume(Message message){
        log.info("消费了消息{}",new String(message.getBody()));
    }
}
