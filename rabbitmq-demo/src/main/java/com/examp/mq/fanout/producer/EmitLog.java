package com.examp.mq.fanout.producer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 模拟日志发送到交换机
 */
public class EmitLog {
    static final String EXCHANGE_NAME = "my-exchange";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.getChannel();
        //声明一个交换机  名称和类型
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //如果交换机声明了就不用再声明了
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.next();
            //routingbind为""
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发生完毕...");
        }
    }
}
