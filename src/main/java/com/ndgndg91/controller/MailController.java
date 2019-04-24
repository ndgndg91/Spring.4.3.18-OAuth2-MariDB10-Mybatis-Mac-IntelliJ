package com.ndgndg91.controller;

import com.ndgndg91.common.MailHandler;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import com.google.common.collect.ImmutableMap;

@Controller
public class MailController {
    @Inject
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration freeMarkerConfigurationFactory;


    @RequestMapping(value = "/mail/test")
    public String mailTest() throws MessagingException, UnsupportedEncodingException {
        MailHandler mailHandler = new MailHandler(javaMailSender);
        mailHandler.setSubject("동길이가 동길이에게 테스트!");
        mailHandler.setFrom("ndgndg91@gmail.com", "남동길");
        mailHandler.setTo("donggil@sixshop.com");
        mailHandler.setText(new StringBuffer().append("<h1>메일인증</h1>")
                .append("테스트 메일입니다. 가입해주셔서 감사합니다.<br><a href='http://localhost:8080")
                .append("' target='_blenk'>메인으로 가기</a>").toString());
        mailHandler.send();
        return "redirect:/";
    }

    @RequestMapping(value = "/mail/freemarker")
    public String freemarkerMailTest() throws IOException, TemplateException, MessagingException {
        final Template hello = freeMarkerConfigurationFactory.getTemplate("hello.ftl");
        String contents = FreeMarkerTemplateUtils.processTemplateIntoString(hello,ImmutableMap.of("message","동범아 동길이의 메세지야 using freemarker"));
        MailHandler mailHandler = new MailHandler(javaMailSender);
        mailHandler.setSubject("동길이가 동길이에게 테스트!");
        mailHandler.setFrom("ndgndg91@gmail.com", "남동길");
        mailHandler.setTo("kimdb35@naver.com");
        mailHandler.setText(contents);
        mailHandler.send();
        return "redirect:/";
    }

}
