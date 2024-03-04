package com.ecommerce.shopapp.services.email;

import com.ecommerce.shopapp.models.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMail(EmailDetails details)
    {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Growth");
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error when sent email");
        }
    }

    public void sendHtmlEmail(EmailDetails details) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress("Growth"));
        message.setRecipients(MimeMessage.RecipientType.TO, details.getRecipient());
        message.setSubject(details.getSubject());
        message.setContent(details.getMsgBody(), "text/html; charset=utf-8");

        javaMailSender.send(message);
    }
}