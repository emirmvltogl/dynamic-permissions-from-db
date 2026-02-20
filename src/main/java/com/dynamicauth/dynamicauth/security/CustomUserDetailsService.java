package com.dynamicauth.dynamicauth.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dynamicauth.dynamicauth.model.User;
import com.dynamicauth.dynamicauth.repository.UserRepository;

@Service
public class CustomUserDetailsService implements  UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
            System.out.println("User : "+user);
    System.out.println("Username : "+username);

    return new CustomUserDetails(user);

  }

}
