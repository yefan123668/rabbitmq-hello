package com.example.demo.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class MyCallback implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    @Autowired
    RabbitTemplate template;
    /*需要将这两个回调接口注入到template里*/
    @PostConstruct
    public void init(){
        template.setConfirmCallback(this);
        template.setReturnCallback(this);
    }
    /*消息确认回调方法*/
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId():"";
        if (ack){
            log.info("消息ID为：{},已被确认",id);
        } else {
            log.info("消息ID为：{},没有被确认",id);
            log.info("，原因是：{}",cause);
        }
    }
    /*队列出现问题，消息被交换机回退给生产者*/
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息{}，被交换机{}退回，退回原因是{}，退回路由是{}",new String(message.getBody()),exchange,replyText,routingKey);
    }
}
