package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing client-related operations.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditController {

  private final CreditService creditService;

  @PostMapping
  public Mono<CreditResponse> createCredit(@Valid @RequestBody CreditRequest request) {
    return creditService.createCredit(request);
  }

  @GetMapping("/{id}")
  public Mono<CreditResponse> getCreditById(@PathVariable String id) {
    return creditService.getCreditById(id);
  }

  @PutMapping("/{id}")
  public Mono<CreditResponse> updateCredit(@PathVariable String id, @Valid @RequestBody CreditRequest request) {
    return creditService.updateCredit(id, request);
  }

  @DeleteMapping("/{id}")
  public Mono<Void> deleteCredit(@PathVariable String id) {
    return creditService.deleteCredit(id);
  }
}
