package com.example.hiveinform.service.impl;

import com.example.hiveinform.entity.Group;
import com.example.hiveinform.entity.UserSelfMessage;
import com.example.hiveinform.entity.Message;
import com.example.hiveinform.handler.WebSocketHandler;
import com.example.hiveinform.repository.UserSelfMessageRepository;
import com.example.hiveinform.repository.MessageRepository;
import com.example.hiveinform.service.GroupService;
import com.example.hiveinform.service.MessageService;
import com.example.hiveinform.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private ConnectionFactory connectionFactory ;

    @Autowired
    private RabbitAdmin rabbitAdmin ;

    @Autowired
    private UserService userService ;

    @Autowired
    private GroupService groupService ;

    @Autowired
    private UserSelfMessageRepository userSelfMessageRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private WebSocketHandler webSocketHandler;


    @Override
    public void sendMessage(Message message) throws IOException, TimeoutException {
        Channel channel = connectionFactory.createConnection().createChannel(true);
        try {
            channel.txSelect();
            Group group = groupService.getGroup(message.getGroupId());
            channel.exchangeDeclare("exchange_"+group.getId(), ExchangeTypes.TOPIC,true);
            Message save = messageRepository.save(message);
            String jsonMessage = mapper.writeValueAsString(save) ;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AMQP.BasicProperties properties = null;
            if (authentication != null && authentication.isAuthenticated()&& !authentication.getName().equals("anonymousUser")) {
                long userId = userService.getByUsername(authentication.getName()).getId();
                properties = new AMQP.BasicProperties().builder().replyTo(save.getReplay()).messageId(save.getId()).build();
            }

            channel.basicPublish("exchange_"+group.getId(), "routing_key_"+group.getId(), properties , jsonMessage.getBytes(StandardCharsets.UTF_8));
            channel.txCommit();
        } catch (IOException e) {
            channel.txRollback();
            throw new RuntimeException(e);

        }
        channel.close();
    }

    @Override
    public void listenMessage(long id) throws IOException, TimeoutException {
        Channel channel = connectionFactory.createConnection().createChannel(true);
        try{
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
//                    long deliveryTag =  envelope.getDeliveryTag();
//                    channel.basicAck(deliveryTag, false);
                    String jsonMessage = new String(body, StandardCharsets.UTF_8);

                    webSocketHandler.sendOneMessage(id,jsonMessage);

                }
            };
                channel.basicConsume("queue_"+id, true, consumer);
        } catch (IOException e) {
            channel.close();
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Message> getMessage(long userId, String messageId) {
        // 返回一个 message 的 list
        UserSelfMessage groupSelfMessageByUserId = userSelfMessageRepository.getByUserId(userId);
        if (groupSelfMessageByUserId != null)
        {
            List<Message> messages = groupSelfMessageByUserId.getMessages();
            return messages.subList(messages.indexOf(messageId), messages.size());
        }
        return null;
    }


    /**
     *
     * 弃用
     *
     * **/
    public byte[] objectToByteArray(Serializable obj) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(obj);
            objectStream.close();
            return byteStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * 弃用
     *
     * **/
    public Object byteArrayToString(byte[] byteArray) {
        try {
            ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            Object obj = objectStream.readObject();
            if (obj instanceof String) {
                obj = new String((byte[]) obj, "UTF-8");
            }
            objectStream.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
