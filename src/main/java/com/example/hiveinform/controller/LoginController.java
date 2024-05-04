package com.example.hiveinform.controller;

import com.example.hiveinform.dto.JwtRequest;
import com.example.hiveinform.dto.JwtResponse;
import com.example.hiveinform.entity.JwtToken;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.repository.JwtTokenRepository;
import com.example.hiveinform.service.MessageService;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.service.impl.JwtService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.util.RabbitMQManager;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "/Login")
public class LoginController {

    @Autowired
    private RedisService redisService ;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RabbitMQManager rabbitMQManager;



    @PostMapping("/hello")
    public String hello() {
        return "hello";
    }

    @Autowired
    private MessageService messageService;

    @PostMapping
    public JwtResponse login (@RequestBody JwtRequest request) throws IOException, TimeoutException {
        User user = userService.getByUsername(request.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(request.getPassword(), user.getPassword()))
            {
                rabbitMQManager.createUserQueue(user.getId());
                JwtResponse response = new JwtResponse(jwtService.generateToken(user));
                redisService.save(request.getUsername(), response.getJwttoken(),864500000);   // 保存 user 和 token
                return response;
            }
            throw new RuntimeException("the password or username is error");
        }
        throw new IllegalArgumentException("haven't this user");
    }

    @GetMapping
    @Secured({"ROLE_USER","ROLE_ADMIN", "ROLE_SUPERADMIN"})
    public boolean isLogin()
    {
        return !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");
    }
}
