package com.seguo.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

@Controller
public class HomepageController {
    @GetMapping("index")
    String index() {
        return "index";
    }
    @GetMapping("login")
    String login() {
        return "login";
    }
    @GetMapping("register")
    String register() {
        return "register";
    }
    @Autowired
    private JavaMailSender sender;
    @GetMapping("send-mail")
    @ResponseBody
    public String send() throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(new InternetAddress("admin@example.com", "Admin"));
        helper.setSubject("Hello, example!");
        helper.setTo("example@example.com");
        helper.setText("Thank you for testing!");

        sender.send(message);

        return "send success";
    }
}
