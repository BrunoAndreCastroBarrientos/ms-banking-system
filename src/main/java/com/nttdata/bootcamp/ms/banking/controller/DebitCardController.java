package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.DebitCardService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing debit card-related operations.
 * Provides endpoints for creating, retrieving,
 * blocking, and performing transactions with debit cards.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RestController
@RequestMapping("/api/cards/debit")
@RequiredArgsConstructor
public class DebitCardController {

  private final DebitCardService debitCardService;

  @PostMapping
  public Mono<DebitCardResponse> createDebitCard(@RequestBody DebitCardRequest request) {
    return debitCardService.createDebitCard(request);
  }

  @GetMapping("/{id}")
  public Mono<DebitCardResponse> getDebitCardById(@PathVariable String id) {
    return debitCardService.getDebitCardById(id);
  }

  @PutMapping("/{id}")
  public Mono<DebitCardResponse> updateDebitCard(@PathVariable String id, @RequestBody DebitCardRequest request) {
    return debitCardService.updateDebitCard(id, request);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteDebitCard(@PathVariable String id) {
    return debitCardService.deleteDebitCard(id);
  }
}
