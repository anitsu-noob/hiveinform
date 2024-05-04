package com.example.hiveinform;

import com.example.hiveinform.entity.User;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
public class TestingSomething {




        @Autowired
        private JavaMailSender javaMailSender;


        @Test
        public void sendSimpleMail() throws Exception {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("");
            message.setTo("");
            message.setSubject("主题：简单邮件");
            message.setText("测试邮件内容");

            javaMailSender.send(message);
        }

}
