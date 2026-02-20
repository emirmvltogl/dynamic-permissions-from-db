package com.dynamicauth.dynamicauth.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dynamicauth.dynamicauth.model.Role;
import com.dynamicauth.dynamicauth.model.User;
import com.dynamicauth.dynamicauth.repository.RoleRepository;
import com.dynamicauth.dynamicauth.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PermissionService permissionService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Username not found"));
    }

    public User createUser(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new RuntimeException("User must have at least one role");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> rolesFromDb = new HashSet<>();

        for (Role role : user.getRoles()) {
            Role existingRole = roleRepository.findByName(role.getName())
                    .orElseThrow(() ->
                            new RuntimeException("Role not found: " + role.getName()));
            rolesFromDb.add(existingRole);
        }

        user.setRoles(rolesFromDb);

        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {

        User existingUser = getUserById(id);

        existingUser.setEmail(updatedUser.getEmail());

        return userRepository.save(existingUser);
    }

    public void changePassword(Long userId, String oldPassword, String newPassword) {

        User user = getUserById(userId);

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void addRoleToUser(Long userId, String roleName) {

        User user = getUserById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found: " + roleName));

        if (user.getRoles().contains(role)) {
            throw new RuntimeException("User already has this role");
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    public void removeRoleFromUser(Long userId, String roleName) {

        User user = getUserById(userId);

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role not found: " + roleName));

        if (!user.getRoles().contains(role)) {
            throw new RuntimeException("User does not have this role");
        }

        user.getRoles().remove(role);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(id);
    }
}
