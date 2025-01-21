package com.nttdata.bootcamp.ms.banking.service;

import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface JwtService {
  String generateToken(String username);
  String extractUsername(String token);
  Boolean validateToken(String token, String username);

}



