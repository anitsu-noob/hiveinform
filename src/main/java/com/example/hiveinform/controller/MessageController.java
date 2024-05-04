package com.example.hiveinform.controller;

import com.example.hiveinform.dto.MessageDto;
import com.example.hiveinform.entity.Message;
import com.example.hiveinform.service.MessageService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.util.RabbitMQManager;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private Map<String, String> userSubscriptions = new ConcurrentHashMap<>();

    @Autowired
    private RabbitMQManager rabbitMQManager;
    @Autowired
    private UserService userService ;
    @Autowired
    private MessageService messageService ;

    
    @PostMapping
    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public void sendMessage(@RequestBody MessageDto messageDto) throws IOException, TimeoutException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated() && authentication != null
                && userService.getByUsername(authentication.getName()).getId() == messageDto.getUserId())
        {
            Message message = new Message(null, messageDto.getGroupId(), messageDto.getContent(), messageDto.getUserId(),
                    LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()),true, messageDto.getReplay(), messageDto.getAtUser());

            messageService.sendMessage(message);
        }
        else {
            throw new IllegalArgumentException("the Message couldn't be confirm the security : " + messageDto.getId());
        }
    }

    @GetMapping
    public void ListenMessage() throws IOException, TimeoutException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication != null)
        {
             long id = userService.getByUsername(authentication.getName()).getId();
             messageService.listenMessage(id);
        }
        else
            throw new IllegalArgumentException("the security : " + authentication.getName() + " is not authenticated");
    }

    @GetMapping("/get/{userId}/{messgaeId}")
    public Object getMessage(@PathVariable long userId,@PathVariable String messageId)
    {
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication != null)
        {
            return messageService.getMessage(userId,messageId) ;
        }
        else
           throw new IllegalArgumentException("the exchange not binded to the queue") ;
    }


//    @RabbitListener(queues = "#{rabbitMQManager.getQueueName()}")
//    public void handleMessage(String message) {
//            messagingTemplate.convertAndSendToUser( SecurityContextHolder.getContext().getAuthentication().getName(),
//                    "/topic/myTopic", message);
//    }



}