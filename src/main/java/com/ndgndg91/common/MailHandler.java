package com.ndgndg91.common;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

public class MailHandler {
    private JavaMailSender javaMailSender;
    private MimeMessage message;
    private MimeMessageHelper mimeMessageHelper;

    public MailHandler(JavaMailSender mailSender) throws MessagingException {
        this.javaMailSender = mailSender;
        this.message = mailSender.createMimeMessage();
        this.mimeMessageHelper = new MimeMessageHelper(this.message, true, "UTF-8");
    }

    public void setSubject(String subject) throws MessagingException {
        this.mimeMessageHelper.setSubject(subject);
    }

    public void setText(String htmlContent) throws MessagingException {
        this.mimeMessageHelper.setText(htmlContent, true);
    }

    public void setFrom(String email, String name) throws MessagingException, UnsupportedEncodingException {
        this.mimeMessageHelper.setFrom(email, name);
    }

    public void setTo(String email) throws MessagingException {
        this.mimeMessageHelper.setTo(email);
    }

    public void addInline(String contentId, DataSource dataSource) throws MessagingException {
        this.mimeMessageHelper.addInline(contentId, dataSource);
    }
    public void send() {
        this.javaMailSender.send(message);
    }

}