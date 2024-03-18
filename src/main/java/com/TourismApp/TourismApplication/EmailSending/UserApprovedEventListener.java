package com.TourismApp.TourismApplication.EmailSending;

import com.TourismApp.TourismApplication.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class UserApprovedEventListener implements ApplicationListener<UserApprovedEvent> {
    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(UserApprovedEvent event) {
        String userEmail = event.getUserEmail();
        String subject = "Account Approved";
        String body = "Dear user, your account has been approved. You can now log in.";
        emailService.sendEmail(userEmail, subject, body);
    }
}
