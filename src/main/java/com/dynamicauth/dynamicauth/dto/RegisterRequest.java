package com.dynamicauth.dynamicauth.dto;

import lombok.Data;

@Data
public class RegisterRequest {
  private String username;
  private String password;
  private String email;
  private String roleName; // e.g., "ADMIN", "USER"
}
