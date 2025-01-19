package com.nttdata.bootcamp.ms.banking.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.function.Function;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Service
public class JwtService {

  @Value("${spring.secret.key}")
  private String secretKey;

  public String generateToken(String username) {
    SecretKey secretKeyValue = Keys.hmacShaKeyFor(secretKey.getBytes());
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
        .signWith(secretKeyValue, SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUsername(String token) {
    return extractClaim(token, claims -> claims.getSubject());
  }

  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  private <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver) {
    SecretKey secretKeyValue = Keys.hmacShaKeyFor(secretKey.getBytes());
    final var claims = Jwts.parserBuilder()
        .setSigningKey(secretKeyValue)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claimsResolver.apply(claims);
  }

  private Boolean isTokenExpired(String token) {
    return extractClaim(token, claims -> claims.getExpiration()).before(new Date());
  }
}



