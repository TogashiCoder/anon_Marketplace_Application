package com.marketplace.controller;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class EmailController {

    private final JavaMailSender mailSender;

    @RequestMapping("/send-email")
    public String sendEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("d@gmail.com");
            message.setSubject("From Taoufik");
            message.setText("Hello FatGirl this is a message from Programer Taoufik");
            mailSender.send(message);
            return "Email sent successfully";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    @RequestMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ibrahimsilver007@gmail.com");
            helper.setTo("d@gmail.com");
            helper.setSubject("Java email with attachment | From GC");
            helper.setText("Please find the attached documents below");

            helper.addAttachment("logo.png", new File("C:\\Users\\Genuine Coder\\Documents\\Attachments\\logo.png"));
            helper.addAttachment("presentation.pptx", new File("C:\\Users\\Genuine Coder\\Documents\\Attachments\\presentation.pptx"));

            mailSender.send(message);
            return "success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/send-html-email")
    public String sendHtmlEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ibrahimsilver007@gmail.com");
            helper.setTo("imaneboucetta69@gmail.com");
            helper.setSubject("Java email with attachment | From Taoufik");

            try (var inputStream = Objects.requireNonNull(EmailController.class.getResourceAsStream("/templates/email-welcome.html"))) {
                helper.setText(
                        new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                        true
                );
            }
           // helper.addInline("logo.png", new File("C:\\Users\\Genuine Coder\\Documents\\Attachments\\logo.png"));
            mailSender.send(message);
            return "success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }



}
