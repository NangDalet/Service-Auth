package com.dt.student.register.authentication.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceUtil {

    private JavaMailSender javaMailSender;

    @Autowired
    public void EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public EmailServiceUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String email, String verificationCode) {
        String subject = "Email Verification Code - EMR System";
        String htmlMessage = generateEmailTemplate(verificationCode, "Email Verification", "Thank you for registering with <strong>EMR System</strong>. Please enter the verification code below.");
        sendEmail(email, subject, htmlMessage);
    }

    public void sendForgotPasswordEmail(String email, String verificationCode) {
        String subject = "Reset Password Code - EMR System";
        String htmlMessage = generateEmailTemplate(verificationCode, "Password Reset Request", "We have received a request to reset your password. Please use the following verification code to reset your password.");
        sendEmail(email, subject, htmlMessage);
    }

    private void sendEmail(String toEmail, String subject, String htmlMessage) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlMessage, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateEmailTemplate(String code, String title, String message) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;}" +
                ".email-container {max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.1); padding: 10px;}" +
                ".header {background-color: #222263; padding: 10px 20px; color: #ffffff; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px;}" +
                ".header h1 {margin: 0; font-size: 18px; font-weight: bold;}" +
                ".content {padding: 20px;}" +
                ".content h2 {font-size: 16px; color: #333333; margin-top: 0;}" +
                ".content p {font-size: 14px; color: #555555; line-height: 1.6;}" +
                ".content .code-box {background-color: #f7f9fc; padding: 15px; border-radius: 8px; text-align: center; margin: 20px 0;}" +
                ".content .code-box p {font-size: 22px; font-weight: bold; color: #007bff; margin: 0;}" +
                ".footer {padding: 20px; text-align: center; color: #999999; font-size: 14px;}" +
                ".footer p {margin: 0;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='email-container'>" +
                "<div class='header'>" +
                "<h1>UDAYA Technology Co., Ltd</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>" + title + "</h2>" +
                "<p>" + message + "</p>" +
                "<div class='code-box'><p>" + code + "</p></div>" +
                "<p>If you did not request this, please ignore this email or contact our support team.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 UDAYA Technology Co., Ltd. All rights reserved.</p>" +
                "<p><a href='info@udaya-tech.com'>Contact Support</a></p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}

