package com.ecommerce.shopapp.services.email;

import com.ecommerce.shopapp.models.EmailDetails;
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
            System.out.println("End setting");

            javaMailSender.send(mailMessage);
            System.out.println("Sent Email at EMAIL");
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error when sent email");
        }
    }
}
