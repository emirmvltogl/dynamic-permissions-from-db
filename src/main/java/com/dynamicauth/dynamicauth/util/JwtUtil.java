package com.dynamicauth.dynamicauth.util;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
  
  @Value("${jwt.secret}")
  private String secret;

  private Key key;

  private long expirationTime = 10*60*60*1000;// 10 saat


  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(secret.getBytes());
}

  public String greateToken(UserDetails userDetails){
    Map<String, Object> claims = new HashMap<>();

    Collection<? extends GrantedAuthority> auth = userDetails.getAuthorities();

    List<String> roles = auth.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    claims.put("roles", roles);

    return Jwts.builder().subject(userDetails.getUsername()).claims(claims)
    .signWith(key).expiration(new Date(System.currentTimeMillis()+expirationTime)).compact();
  }

  public Claims extractAllClaims(String token){
    return Jwts.parser().verifyWith((SecretKey)key).build().parseSignedClaims(token).getPayload();
  }

  public String extractUsername(String token){
    return extractAllClaims(token).getSubject();
  }

  public String extractRoles(String token){
    return (String) extractAllClaims(token).get("roles");
  }

  public boolean isTokenExpired(String token){
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  public boolean validateToken(String token, UserDetails userDetails){
    String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }


}
