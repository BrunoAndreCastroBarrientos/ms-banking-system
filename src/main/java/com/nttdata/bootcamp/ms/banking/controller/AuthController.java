package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.UserRequest;
import com.nttdata.bootcamp.ms.banking.entity.User;
import com.nttdata.bootcamp.ms.banking.repository.UserRepository;
import com.nttdata.bootcamp.ms.banking.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for managing account-related operations.
 *
 * <p>This controller provides endpoints for creating,
 * updating, retrieving, and closing accounts.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @PostMapping("/register")
  public Mono<User> register(@RequestBody UserRequest request) {
    User newUser = new User();
    newUser.setUsername(request.getUsername());
    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
    newUser.setRole("ROLE_USER");
    return userRepository.save(newUser);
  }

  @PostMapping("/login")
  public Mono<String> login(@RequestBody UserRequest request) {
    return userRepository.findByUsername(request.getUsername())
        .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
        .map(user -> jwtService.generateToken(user.getUsername()))
        .switchIfEmpty(Mono.error(new RuntimeException("Invalid username or password")));
  }
}
