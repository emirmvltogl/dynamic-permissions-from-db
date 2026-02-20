package com.dynamicauth.dynamicauth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

  @Value("${spring.mail.username}")
  private String from;

  private final JavaMailSender mailSender;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendPasswordResetEmail(String to, String token) {

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      String resetLink = "http://localhost:8080/api/reset-password?token=" + token;

      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject("Password Reset Request");
      helper.setText(
          "<p>You requested a password reset.</p>" +
              "<p>Click the link below to reset your password:</p>" +
              "<a href=\"" + resetLink + "\">Reset Password</a>",
          true);

      mailSender.send(message);

    } catch (Exception e) {
      throw new RuntimeException("Failed to send email", e);
    }
  }
}
