package com.dynamicauth.dynamicauth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dynamicauth.dynamicauth.dto.ResetPasswordDto;
import com.dynamicauth.dynamicauth.dto.ResetPasswordRequest;
import com.dynamicauth.dynamicauth.service.PasswordResetService;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

  private final PasswordResetService passwordResetService;

  public PasswordResetController(PasswordResetService passwordResetService) {
    this.passwordResetService = passwordResetService;
  }

  @PostMapping("/send-reset-email")
  public ResponseEntity<String> sendResetEmail(@RequestBody ResetPasswordRequest request) {
    String email = request.getEmail();
  return ResponseEntity.ok(passwordResetService.sendMail(request.getEmail()));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody  ResetPasswordDto request) {
    return ResponseEntity.ok(passwordResetService.resetPassword(token, request.getPassword()));
  }



}
