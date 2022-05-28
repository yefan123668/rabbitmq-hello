package com.example.demo.delayQueue;

import com.example.demo.common.constants.AMQPConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/delay")
@Slf4j
public class DelayProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/mail/{message}/{delayTime}")
    public String sendMsg(@PathVariable("message") String msg, @PathVariable("delayTime") Integer delayTime){
        log.info("在{},发送了消息为：{}，延迟时间为：{}",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).toString(),
                msg,delayTime);

        rabbitTemplate.convertAndSend(AMQPConstants.DELAY_EXCHANGE,
                AMQPConstants.DELAY_ROUTING_KEY,
                "发送了的数据:"+msg+"延迟为："+delayTime+"ms",
                message -> {
                    //设置延迟时间
                    message.getMessageProperties().setDelay(delayTime);
                    return message;
                });

        return msg;
    }

}
