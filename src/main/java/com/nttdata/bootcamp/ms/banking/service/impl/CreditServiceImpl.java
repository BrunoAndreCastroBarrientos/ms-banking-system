package com.nttdata.bootcamp.ms.banking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CreditMapper;
import com.nttdata.bootcamp.ms.banking.repository.CreditRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

/**
 * Implementación del servicio de créditos. Proporciona operaciones
 * para crear, consultar, pagar, cancelar créditos y verificar morosidad.
 *
 * <p>Este servicio gestiona los créditos de los clientes, incluyendo
 * la validación de las reglas de negocio antes de crear un crédito,
 * como verificar la morosidad del cliente y las restricciones de
 * crédito para clientes personales.
 * Además, permite realizar pagos, cancelar créditos y
 * consultar créditos activos de un cliente.</p>
 *
 * @version 1.1
 */
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

  private final CreditRepository creditRepository;
  private final CreditMapper creditMapper;
  private final WebClient webClient;

  @Value("${service.customers.url}")
  private String customersServiceUrl;


  @Override
  public Mono<CreditResponse> createCredit(CreditRequest request) {
    Credit credit = creditMapper.toEntity(request);

    return validateBeforeCreating(credit)
        .then(creditRepository.save(credit))
        .map(creditMapper::toResponse);
  }

  @Override
  public Mono<CreditResponse> getCreditById(String creditId) {
    return creditRepository.findById(creditId)
        .map(creditMapper::toResponse);
  }

  @Override
  public Flux<CreditResponse> getCreditsByCustomerId(String customerId) {
    return creditRepository.findByCustomerId(customerId)
        .map(creditMapper::toResponse);
  }

  @Override
  public Mono<Void> cancelCredit(String creditId) {
    return creditRepository.findById(creditId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Credit not found: " + creditId)))
        .flatMap(credit -> {
          if (credit.getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0) {
            return Mono.error(
                new ApiValidateException("Cannot cancel credit with outstanding balance."));
          }
          credit.setStatus(RecordStatus.CANCELLED);
          return creditRepository.save(credit);
        })
        .then();
  }

  @Override
  public Mono<Boolean> hasOverdueDebt(String customerId) {
    return creditRepository.findByCustomerId(customerId)
        .filter(this::isOverdue)
        .hasElements();
  }

  @Override
  public Mono<CreditResponse> applyPayment(String creditId, Double paymentAmount) {
    return creditRepository.findById(creditId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Credit not found: " + creditId)))
        .flatMap(credit -> {
          BigDecimal payment = BigDecimal.valueOf(paymentAmount);
          if (payment.compareTo(BigDecimal.ZERO) <= 0) {
            return Mono.error(new ApiValidateException("Payment must be greater than 0"));
          }
          BigDecimal newBalance = credit.getOutstandingBalance().subtract(payment);
          if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
          }
          credit.setOutstandingBalance(newBalance);
          if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            credit.setStatus(RecordStatus.CLOSED);
          }
          return creditRepository.save(credit);
        })
        .map(creditMapper::toResponse);
  }

  private boolean isOverdue(Credit credit) {
    return credit.getDueDate() != null
        && credit.getDueDate().isBefore(java.time.LocalDate.now())
        && credit.getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0;
  }

  private Mono<Void> validateBeforeCreating(Credit credit) {
    return getCustomerTypeAndSubType(credit.getCustomerId())
        .flatMap(tuple -> {
          String customerType = tuple.getT1();
          String subType = tuple.getT2();

          return hasOverdueDebt(credit.getCustomerId())
              .flatMap(hasOverdue -> {
                if (hasOverdue) {
                  return Mono.error(
                      new ApiValidateException("Customer has overdue debt. Cannot create new credit."));
                }
                if (CreditType.PERSONAL == credit.getCreditType()
                    && "PERSONAL".equalsIgnoreCase(customerType)) {
                  return validatePersonalCreditLimit(credit.getCustomerId(), subType);
                }
                return Mono.empty();
              });
        });
  }

  private Mono<Tuple2<String, String>> getCustomerTypeAndSubType(String customerId) {
    return webClient.get()
        .uri(customersServiceUrl + "/api/customers/" + customerId)
        .retrieve()
        .bodyToMono(JsonNode.class)
        .map(response -> {
          String customerType = response.path("customerType").asText();
          String subType = response.path("subType").asText();
          return Tuples.of(customerType, subType);
        });
  }

  private Mono<Void> validatePersonalCreditLimit(String customerId, String subType) {
    if ("STANDARD".equalsIgnoreCase(subType)) {
      return creditRepository.findByCustomerId(customerId)
          .filter(c -> c.getCreditType() == CreditType.PERSONAL
              && (c.getStatus() == RecordStatus.ACTIVE || c.getStatus() == RecordStatus.BLOCKED))
          .count()
          .flatMap(count -> {
            if (count >= 1) {
              return Mono.error(
                  new ApiValidateException("STANDARD personal customer already has a personal credit."));
            }
            return Mono.empty();
          });
    }
    return Mono.empty();
  }
}
