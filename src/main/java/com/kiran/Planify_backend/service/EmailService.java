package com.kiran.Planify_backend.service;

import com.kiran.Planify_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    private void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendVerificationEmail(User user, String frontendUrl) {
        String verificationLink = frontendUrl + "/api/auth/verify?token=" + user.getVerificationToken();
        String message = "Hi " + user.getName() + ",\n\nPlease verify your email using this link:\n" + verificationLink;

        System.out.println("Sending verification email to: " + user.getEmail());
        System.out.println("Verification link: " + verificationLink);

        // ✅ Send the actual email
        sendEmail(user.getEmail(), "Email Verification - SubPilot", message);
    }


    public void sendSubscriptionConfirmation(User user){
        String msg = "Hi " + user.getName() + ",\n\nYou have successfully subscribed to the "
                + user.getPlan().getName() + " plan. Valid until: " + user.getSubscriptionExpiry();

        sendEmail(user.getEmail(), "Subscription Confirmed - Planify", msg);

    }

    private void sendSubscriptionReminder(User user){
        String msg = "Hi " + user.getName() + "\n\nYour subscription to " +
                user.getPlan().getName() + " will expire on: "+
                user.getSubscriptionExpiry() + ". Please renew soon!";

        sendEmail(user.getEmail(), "Subscription Reminder - Planify", msg);
    }
}
