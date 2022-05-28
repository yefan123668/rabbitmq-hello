package com.example.demo.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListeners;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 延迟队列被消费者消费
 */
@Component
@Slf4j
public class TTLConsumer {

    @RabbitListener(queues = "E") //监听死信队列
    public void parseMsg(Channel channel, Message message){
        System.out.println("====================");
        log.info("在{},接收到了消息{}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString(),
                new String(message.getBody(), StandardCharsets.UTF_8));
    }
}
