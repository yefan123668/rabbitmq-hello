package com.examp.mq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class RabbitmqUtils {

    public static Channel getChannel(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123");
        //设置IP
        factory.setHost("43.142.26.233");
        //获得一个信道,自动close
        Channel channel;
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return channel;
    }
}
