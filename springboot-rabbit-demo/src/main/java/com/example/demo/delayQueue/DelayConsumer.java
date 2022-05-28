package com.example.demo.delayQueue;

import com.example.demo.common.constants.AMQPConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class DelayConsumer {

    @RabbitListener(queues = AMQPConstants.DELAY_QUEUE)
    public void delayToConsumer(Message msg, Channel channel){
        //System.out.println("====================");
        log.info("在{},接收到了消息{}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString(),
                new String(msg.getBody(), StandardCharsets.UTF_8));
    }
}
