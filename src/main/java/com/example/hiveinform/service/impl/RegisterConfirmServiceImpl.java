package com.example.hiveinform.service.impl;

import com.example.hiveinform.entity.EmailSend;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.service.RegisterConfirmService;
import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service("registerConfirmService")
public class RegisterConfirmServiceImpl implements RegisterConfirmService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisService redisService ;

    @Value("${spring.mail.from}")
    private String from;

    @Override
    public EmailSend sendEmail(EmailSend emailSend) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(from);
        messageHelper.setTo(emailSend.getToAddress());
        messageHelper.setSubject(emailSend.getSubject());
        messageHelper.setText(emailSend.getContent(),true);
        javaMailSender.send(mimeMessage);
        return emailSend;
    }

    @Override
    public boolean confirmCode(String code,String email) {
        if (email == null)
            return false;
        if (email.equals(redisService.get(code.toLowerCase()))) {
            return true;
        }
        return false;
    }
}
