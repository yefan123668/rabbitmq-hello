package com.examp.mq.direct.producer;

import com.examp.mq.utils.RabbitmqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DirectLogs {

    private static final String EXCHANGE_NAME = "log-exchange";
    public static void main(String[] args) throws IOException {
        Channel channel = RabbitmqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String msg = scanner.next();
            //发生消息到error
            channel.basicPublish(EXCHANGE_NAME,"error",null,msg.getBytes(StandardCharsets.UTF_8));
        }
    }
}
