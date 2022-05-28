package com.example.demo.controller;

import com.example.demo.common.constants.AMQPConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm")
@Slf4j
public class ConfirmController {

    @Autowired
    RabbitTemplate template;

    /**
     * 发布消息确认
     * @param msg
     * @return
     */
    @GetMapping("message/{msg}")
    public String receive(@PathVariable String msg){
        CorrelationData correlationData = new CorrelationData("1");
        log.info("发送了消息{}",msg);
        template.convertAndSend(AMQPConstants.CONFIRM_EXCHANGE,AMQPConstants.CONFIRM_ROUTING_KEY,msg,correlationData);
        return msg;
    }

    /*消息回退或者交换机备份路由*/
    @GetMapping("warning/{msg}")
    public String receiveWarning(@PathVariable String msg){
        CorrelationData correlationData = new CorrelationData("1");
        log.info("发送了消息{}",msg);
        template.convertAndSend(AMQPConstants.CONFIRM_EXCHANGE,AMQPConstants.CONFIRM_ROUTING_KEY+"1",msg,correlationData);
        return msg;
    }
}
