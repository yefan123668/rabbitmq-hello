package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生产者
 */
@RestController
@RequestMapping("ttl")
@Slf4j
public class RendMsgController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/mail/{message}")
    public String sendMsg(@PathVariable("message") String msg){
        log.info("在{},发送了消息{}",
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString(),
                msg);
        rabbitTemplate.convertAndSend("A","CA","发送了延迟为10s的数据"+msg);
        rabbitTemplate.convertAndSend("A","DA","发送了延迟为30s的数据"+msg);
        return msg;
    }

    @GetMapping("/mail/{message}/{ttlTime}")
    public String sendMsg(@PathVariable("message") String msg,@PathVariable("ttlTime") String ttlTime){
        log.info("在{},发送了消息为：{}，延迟时间为：{}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString(),
                msg,ttlTime);

        rabbitTemplate.convertAndSend("A",
                "FA",
                "发送了的数据:"+msg+"延迟为："+ttlTime+"ms",
                message -> {
                    //设置延迟时间
                    message.getMessageProperties().setExpiration(ttlTime);
                    return message;
                });

        return msg;
    }


}
