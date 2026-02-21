package com.dynamicauth.dynamicauth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dynamicauth.dynamicauth.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final CustomUserDetailsService customUserDetailsService;

  public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
    this.jwtUtil = jwtUtil;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {

      String token = header.substring(7);

      try {
        String username = jwtUtil.extractUsername(token);

        if (username != null
            && !jwtUtil.isTokenExpired(token)
            && SecurityContextHolder.getContext().getAuthentication() == null) {

          CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

          if (jwtUtil.validateToken(token, userDetails)) {

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
          }
        }

      } catch (UsernameNotFoundException e) {
        
        System.out.println("JWT Error: " + e.getMessage());
      }
    }

    filterChain.doFilter(request, response);
  }

}
