package com.dynamicauth.dynamicauth.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dynamicauth.dynamicauth.dto.LoginRequest;
import com.dynamicauth.dynamicauth.dto.LoginResponse;
import com.dynamicauth.dynamicauth.dto.RegisterRequest;
import com.dynamicauth.dynamicauth.model.Role;
import com.dynamicauth.dynamicauth.model.User;
import com.dynamicauth.dynamicauth.security.CustomUserDetailsService;
import com.dynamicauth.dynamicauth.service.PermissionService;
import com.dynamicauth.dynamicauth.service.UserService;
import com.dynamicauth.dynamicauth.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class Auth {

  private final AuthenticationManager authenticationManager;
  private final CustomUserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final PermissionService permissionService;

  public Auth(AuthenticationManager authenticationManager,
      CustomUserDetailsService userDetailsService,
      JwtUtil jwtUtil,
      UserService userService, PermissionService permissionService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtUtil = jwtUtil;
    this.userService = userService;
    this.permissionService = permissionService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    } catch (AuthenticationException ex) {
      ex.printStackTrace();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password: " + ex.getMessage());
    }
    var userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    String token = jwtUtil.greateToken(userDetails);

    return ResponseEntity.ok(new LoginResponse(token));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    try {
      User newUser = new User(request.getUsername(), request.getPassword(), request.getEmail());
      Set<Role> roles = new HashSet<>();
      roles.add(new Role(request.getRoleName()));
      newUser.setRoles(roles);
      User createdUser = userService.createUser(newUser);
      return ResponseEntity.status(HttpStatus.CREATED).body("User registered: " + createdUser.getUsername());
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }


}
