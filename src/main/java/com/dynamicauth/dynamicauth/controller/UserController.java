package com.dynamicauth.dynamicauth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dynamicauth.dynamicauth.model.User;
import com.dynamicauth.dynamicauth.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/username/{username}")
  public ResponseEntity<User> getByUsername(@PathVariable String username) {
    return ResponseEntity.ok(userService.getByUsername(username));
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    return ResponseEntity.ok(userService.createUser(user));
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(
      @PathVariable Long id,
      @RequestBody User updatedUser) {

    return ResponseEntity.ok(userService.updateUser(id, updatedUser));
  }

  @PutMapping("/{id}/change-password")
  public ResponseEntity<Void> changePassword(
      @PathVariable Long id,
      @RequestParam String oldPassword,
      @RequestParam String newPassword) {

    userService.changePassword(id, oldPassword, newPassword);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/roles")
  public ResponseEntity<Void> addRole(
      @PathVariable Long id,
      @RequestParam String roleName) {

    userService.addRoleToUser(id, roleName);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}/roles")
  public ResponseEntity<Void> removeRole(
      @PathVariable Long id,
      @RequestParam String roleName) {

    userService.removeRoleFromUser(id, roleName);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
