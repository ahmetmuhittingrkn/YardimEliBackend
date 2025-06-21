package com.yazilim.afet.service.impl;

import com.yazilim.afet.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String to, String token) {
        String subject = "E-posta Doğrulama";
        String confirmationUrl = "http://localhost:8080/api/auth/confirm?token=" + token;
        String body = "Merhaba,\n\nLütfen hesabınızı doğrulamak için aşağıdaki bağlantıya tıklayın:\n" + confirmationUrl;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}
