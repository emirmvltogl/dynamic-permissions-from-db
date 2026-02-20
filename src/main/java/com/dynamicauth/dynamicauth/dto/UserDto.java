package com.dynamicauth.dynamicauth.dto;

import java.util.Set;

import com.dynamicauth.dynamicauth.model.Role;

import lombok.Data;

@Data
public class UserDto {

  private Long id;
  private String username;
  private String email;
  private Set<Role> roles;

}
