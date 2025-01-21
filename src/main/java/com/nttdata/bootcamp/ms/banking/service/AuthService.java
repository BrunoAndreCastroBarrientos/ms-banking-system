package com.nttdata.bootcamp.ms.banking.service;

import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<String> authenticate(String username, String password);
}





