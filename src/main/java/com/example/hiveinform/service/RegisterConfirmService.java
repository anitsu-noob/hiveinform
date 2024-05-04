package com.example.hiveinform.service;

import com.example.hiveinform.entity.EmailSend;
import jakarta.mail.MessagingException;
import org.hibernate.annotations.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

public interface RegisterConfirmService{

    EmailSend sendEmail(EmailSend emailSend) throws MessagingException;

    boolean confirmCode(String code,String email) ;

}
