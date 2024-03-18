package com.TourismApp.TourismApplication.Services;

import com.TourismApp.TourismApplication.Models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final JavaMailSender emailSender;

    @Autowired
    public ContactService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmailToAgency(Contact contact) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("asmaseghir1770@gmail.com");
        message.setSubject("New Contact Form Collaboration");
        message.setText("Dear Responsible of Tourism Content,\n" +
                "\n" +
                "My name is  " + contact.getFirstName() + " "  + contact.getLastName().toUpperCase() + ", " +
                "from the Company: " + contact.getCompany() + "," +"\n" +
                contact.getMessage() + "\n" +
                "You can contact me from my address Email: " + contact.getEmail() + "," +"\n" +
                "Or from my Phone Number: " + contact.getPhoneNumber() + "\n" +
                "\n" +
                "Cordialement");
        message.setFrom(contact.getEmail());
        emailSender.send(message);
    }
}
