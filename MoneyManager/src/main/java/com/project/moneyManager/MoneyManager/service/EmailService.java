package com.project.moneyManager.MoneyManager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class EmailService {
private final JavaMailSender mailSender;
@Value("${spring.mail.properties.mail.smtp.from}")
private String fromMail;


    public void sendMail(String to,String subject,String body) {
        try{
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(fromMail);
            mailMessage.setSubject(subject);
            mailMessage.setTo(to);
            mailMessage.setText(body);
            mailSender.send(mailMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
