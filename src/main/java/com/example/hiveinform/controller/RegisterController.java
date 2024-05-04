package com.example.hiveinform.controller;

import com.example.hiveinform.dto.JwtResponse;
import com.example.hiveinform.dto.RegisterRequest;
import com.example.hiveinform.dto.UserInformDto;
import com.example.hiveinform.entity.EmailSend;
import com.example.hiveinform.entity.JwtToken;
import com.example.hiveinform.repository.JwtTokenRepository;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.service.RegisterConfirmService;
import com.example.hiveinform.service.UserInformService;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.service.impl.AuthService;
import com.example.hiveinform.util.ContentBuildUtil;
import com.example.hiveinform.util.RabbitMQManager;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private UserService userService ;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserInformService userInformService ;

    @Autowired
    private RegisterConfirmService registerConfirmService ;

    @Autowired
    private AuthService authService;

    @Autowired
    private ContentBuildUtil contentBuildUtil ;


    @Value("${spring.mail.username}")
    private String from ;

    @PostMapping
    public JwtResponse register(@RequestBody RegisterRequest request) throws IOException {
        JwtResponse register = null;
        if (registerConfirmService.confirmCode(request.getCode(),request.getEmail()) ) {
            register = authService.register(request);
            redisService.delete(request.getCode().toLowerCase());
        }
        if (register == null) {
            throw new RuntimeException("register failed");
        }
        return register ;
    }  // 注册即可登录 并创建userInform

    @GetMapping("/{toAddress}")
    public void getConfirmCode(@PathVariable String toAddress) throws MessagingException {
        EmailSend emailSend = new EmailSend();
        emailSend.setFrom(from);
        emailSend.setToAddress(toAddress);
        emailSend.setSubject("邮箱验证码");
        emailSend.setContent(contentBuildUtil.buildContent(emailSend.getToAddress(),"sign up for an account with"));
        registerConfirmService.sendEmail(emailSend);
    }

    @PostMapping("/confirm/{email}/{code}")
    public boolean confirm(@PathVariable String email , @PathVariable String code ) {
       return registerConfirmService.confirmCode(code,email) ;
    }
}
