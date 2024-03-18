package com.TourismApp.TourismApplication.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

 public void sendEmail(String toEmail, String subject, String body){
     try {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom("asmaseghir1770@gmail.com");
         message.setTo(toEmail);
         message.setText(body);
         message.setSubject(subject);
         mailSender.send(message);
         System.out.println("Mail sent successfully to: " + toEmail);
     } catch (Exception e) {
         System.err.println("Error sending email: " + e.getMessage());
     }
}




}
