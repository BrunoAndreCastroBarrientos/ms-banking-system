package com.nttdata.bootcamp.ms.banking.controller;


import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.CreditCardService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing credit card related operations.
 *
 * <p>This controller provides endpoints for creating,
 * retrieving, updating, and blocking credit cards.</p>
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@RestController
@RequestMapping("/api/cards/credit")
@RequiredArgsConstructor
public class CreditCardController {

  private final CreditCardService creditCardService;

  @PostMapping
  public Mono<CreditCardResponse> createCreditCard(@Valid @RequestBody CreditCardRequest request) {
    return creditCardService.createCreditCard(request);
  }

  @GetMapping("/{id}")
  public Mono<CreditCardResponse> getCreditCardById(@PathVariable String id) {
    return creditCardService.getCreditCardById(id);
  }

  @PutMapping("/{id}")
  public Mono<CreditCardResponse> updateCreditCard(@PathVariable String id, @Valid @RequestBody CreditCardRequest request) {
    return creditCardService.updateCreditCard(id, request);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteCreditCard(@PathVariable String id) {
    return creditCardService.deleteCreditCard(id);
  }
}