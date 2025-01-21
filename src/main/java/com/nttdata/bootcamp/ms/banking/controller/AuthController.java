package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.UserRequest;
import com.nttdata.bootcamp.ms.banking.entity.User;
import com.nttdata.bootcamp.ms.banking.repository.UserRepository;
import com.nttdata.bootcamp.ms.banking.service.JwtService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
/**
 * Controller for managing authentication operations.
 *
 * <p>This controller provides endpoints for user registration
 * and login functionality.</p>
 *
 * @version 1.1
 * @author
 * Bruno Andre Castro Barrientos
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Endpoints for user authentication")
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  /**
   * Registers a new user.
   *
   * @param request the user registration details
   * @return the registered user
   */
  @Operation(summary = "Register User", description = "Registers a new user with the provided details.")
  @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = ConstantUtil.ERROR_MESSAGE)
  @PostMapping("/register")
  public Mono<User> register(@Valid @RequestBody UserRequest request) {
    User newUser = new User();
    newUser.setUsername(request.getUsername());
    newUser.setPassword(passwordEncoder.encode(request.getPassword()));
    newUser.setRole("ROLE_USER");
    return userRepository.save(newUser);
  }

  /**
   * Logs in an existing user.
   *
   * @param request the user login details
   * @return a JWT token if the login is successful
   */
  @Operation(summary = "Login User", description = "Authenticates a user and returns a JWT token.")
  @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = ConstantUtil.OK_MESSAGE)
  @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Invalid username or password.")
  @PostMapping("/login")
  public Mono<String> login(@Valid @RequestBody UserRequest request) {
    return userRepository.findByUsername(request.getUsername())
        .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
        .map(user -> jwtService.generateToken(user.getUsername()))
        .switchIfEmpty(Mono.error(new RuntimeException("Invalid username or password")));
  }
}

