package com.geekbrains.decembermarket.services;

import com.geekbrains.decembermarket.entites.Order;
import com.geekbrains.decembermarket.entites.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    private JavaMailSender sender;
    private MailMessageBuilder messageBuilder;

    @Autowired
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setMessageBuilder(MailMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    private void sendMail(String email, String subject, String text) throws MessagingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setText(text, true);
        helper.setSubject(subject);
        sender.send(message);
    }

    public void sendOrderMail(String code, User user) {
        try {
            sendMail(user.getEmail(), String.format("Ваш код подвтверждения %s", code), messageBuilder.buildOrderEmail(code));
        } catch (MessagingException e) {

        } catch (MailException e) {

        }
    }
}