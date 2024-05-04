package com.example.hiveinform.util;
import com.example.hiveinform.service.UserService;
import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.Topic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQManager {


    @Autowired
    private  RabbitAdmin rabbitAdmin;

    @Autowired
    private  RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory ;

    @Autowired
    private UserService userService;

    public static Map<String,String> userQueue = new HashMap<String,String>();

    public void createGroupExchange(long userId ,String exchangeName) throws IOException {
        Channel channel = connectionFactory.createConnection().createChannel(true);
        Exchange exchange = new TopicExchange("exchange_" + exchangeName);
        rabbitAdmin.declareExchange(exchange) ;

        channel.queueBind("queue_" + userId, "exchange_" + exchangeName,"routing_key_" + exchangeName);
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUserQueue(long userId) {
        try(Channel channel = connectionFactory.createConnection().createChannel(true);){
            channel.queueDeclarePassive("queue_" +userId);
        }catch (Exception e) {
            Queue queue = new Queue("queue_" +userId);
            userQueue.put(userService.getUserById(userId).getUsername(),"queue_" +userId );
            rabbitAdmin.declareQueue(queue);
        }
    }

    public void addUserToGroup(long userId, String exchange) throws IOException {
        Channel channel = connectionFactory.createConnection().createChannel(true);
        channel.exchangeDeclarePassive("exchange_" + exchange) ;   // 没发现就会报错
        channel.queueBind("queue_" + userId,"exchange_" + exchange,"routing_key_" + exchange);
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserFromGroup(long userId, String exchangeName) throws IOException {
        Channel channel = connectionFactory.createConnection().createChannel(true);
        channel.exchangeDeclarePassive("exchange_" + exchangeName) ;
        channel.queueUnbind("queue_" + userId, "exchange_" + exchangeName,"routing_key_" + exchangeName) ;
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteExchange (List<Long> userIds, String exchangeName) throws IOException {
        Channel channel = connectionFactory.createConnection().createChannel(true);
        channel.exchangeDeclarePassive("exchange_" + exchangeName) ;

        for (Long userId : userIds) {
            channel.queueUnbind("queue_" + userId , "exchange_" + exchangeName,"routing_key_" +exchangeName);
        }

        channel.exchangeDelete("exchange_" + exchangeName) ;  //删除了交换机
        try {
            channel.close();
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }


    public void removeUserQueue (long userId) throws IOException {
        try(Channel channel = connectionFactory.createConnection().createChannel(true);){
            channel.queueDeclarePassive("queue_" +userId);
            rabbitAdmin.deleteQueue("queue_" +userId);
            userQueue.remove(userService.getUserById(userId).getUsername());
        }catch (Exception e) {
            throw new IOException("have not this queue ");
        }
    }

    public static String getQueueName() throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) { return userQueue.get(authentication.getName());}
        else  return  "queue_43" ;

    }
}