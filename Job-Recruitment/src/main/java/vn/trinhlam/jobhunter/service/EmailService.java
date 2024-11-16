package vn.trinhlam.jobhunter.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final MailSender mailSender;

    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("trinhquanglam2k4@gmail.com");
        msg.setText("Hello World");
        msg.setSubject("Test");
        this.mailSender.send(msg);
    }
}
