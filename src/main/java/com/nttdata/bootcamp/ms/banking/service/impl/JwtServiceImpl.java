package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionBootCoinType;
import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.entity.BootCoinTransaction;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.BootCoinMapper;
import com.nttdata.bootcamp.ms.banking.mapper.WalletMapper;
import com.nttdata.bootcamp.ms.banking.repository.BootCoinRepository;
import com.nttdata.bootcamp.ms.banking.repository.WalletRepository;
import com.nttdata.bootcamp.ms.banking.service.JwtService;
import com.nttdata.bootcamp.ms.banking.service.WalletService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;


/**
 * Implementación del servicio de tarjetas de crédito.
 * Proporciona operaciones para crear, consultar,
 * actualizar el saldo y bloquear tarjetas de crédito.
 *
 * <p>Esta clase utiliza un repositorio de tarjetas de
 * crédito y un mapper para convertir entre objetos de
 * dominio y DTOs. Además, verifica si un cliente tiene
 * deudas pendientes antes de permitir la creación
 * de una nueva tarjeta.</p>
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  @Value("${spring.secret.key}")
  private String secretKey;

  @Override
  public String generateToken(String username) {
    SecretKey secretKeyValue = Keys.hmacShaKeyFor(secretKey.getBytes());
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
        .signWith(secretKeyValue, SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, claims -> claims.getSubject());
  }

  @Override
  public Boolean validateToken(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
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