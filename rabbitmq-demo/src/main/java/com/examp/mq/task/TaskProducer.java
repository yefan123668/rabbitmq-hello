package com.examp.mq.task;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;

public class TaskProducer {

    static final String QUEUE_NAME = "my-first-mq-prod";

    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.next();
            //消息持久化声明
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
            System.out.println("消息发生完毕...");
        }
    }
}
