package com.dynamicauth.dynamicauth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dynamicauth.dynamicauth.model.User;
import com.dynamicauth.dynamicauth.repository.UserRepository;
import com.dynamicauth.dynamicauth.util.TokenUtil;

@Service
public class PasswordResetService {

  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public PasswordResetService(MailService mailService, PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.mailService = mailService;
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  public String sendMail(String email) {

    if (email == null) {
      throw new IllegalArgumentException("Email cannot be null");
    }
    User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Email not found"));

    String token = TokenUtil.generateToken();
    user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
    user.setResetToken(token);
    userRepository.save(user);

    mailService.sendPasswordResetEmail(email, token);

    return "Password reset email sent to " + email;
  }

  public String resetPassword(String token, String newPassword) {

    User user = userRepository.findByResetToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid token"));

    if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Token has expired");
    }

    user.setPassword(passwordEncoder.encode(newPassword));
    user.setResetToken(null);
    user.setResetTokenExpiry(null);
    userRepository.save(user);

    return "Password has been reset successfully";

  }

}
