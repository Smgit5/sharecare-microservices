package com.suman.sharecare.auth.service;

import com.suman.sharecare.auth.entity.EmailVerificationToken;
import com.suman.sharecare.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${SHARECARE_DEV_URL}")
    private String SHARECARE_BASE_URL;

    private final JavaMailSender mailSender;

    private void sendEMail(String to, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        mailSender.send(simpleMailMessage);
    }

    public void sendVerificationEmail(EmailVerificationToken emailVerificationToken) {
        User receiver = emailVerificationToken.getUser();
        String username = receiver.getUsername();
        String emailVerificationUrl = SHARECARE_BASE_URL + "/verify-email?token=" + emailVerificationToken.getToken();
        String subject = "Verify your ShareCare account.";
        String body = """
                Hello %s,
                Welcome to ShareCare!
                Please verify your email by clicking the link below:
                
                %s
                
                If you did not create this account, simply ignore this email.
                """.formatted(username, emailVerificationUrl);

        String receiverEmail = receiver.getEmail();
        sendEMail(receiverEmail, subject, body);
    }
}
