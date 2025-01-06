package com.nttdata.bootcamp.ms.banking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CreditMapper;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.repository.CreditRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

  private final CreditRepository creditRepository;
  private final CreditMapper creditMapper;

  // URL del microservicio de Clientes (para saber si es estándar o VIP, personal o empresarial)
  @Value("${service.customers.url}")
  private String customersServiceUrl;

  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<CreditResponse> createCredit(CreditRequest request) {
    // 1. Mapeamos request a la entidad
    Credit credit = creditMapper.requestToEntity(request);

    // 2. Validaciones de negocio
    return validateBeforeCreating(credit)
        // 3. Guardar
        .then(creditRepository.save(credit))
        // 4. Convertir a Response
        .map(creditMapper::entityToResponse);
  }

  @Override
  public Mono<CreditResponse> getCreditById(String creditId) {
    return creditRepository.findById(creditId)
        .map(creditMapper::entityToResponse);
  }

  @Override
  public Flux<CreditResponse> getCreditsByCustomerId(String customerId) {
    return creditRepository.findByCustomerId(customerId)
        .map(creditMapper::entityToResponse);
  }

  @Override
  public Mono<Void> cancelCredit(String creditId) {
    return creditRepository.findById(creditId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Credit not found: " + creditId)))
        .flatMap(credit -> {
          // Regla: si outstandingBalance > 0, no se puede cancelar
          if (credit.getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0) {
            return Mono.error(new ApiValidateException("Cannot cancel credit with outstanding balance."));
          }
          credit.setStatus(RecordStatus.CANCELLED);
          return creditRepository.save(credit);
        })
        .then();
  }

  @Override
  public Mono<Boolean> hasOverdueDebt(String customerId) {
    // Se buscarán créditos activos del cliente y se valida si alguno está "moroso"
    return creditRepository.findByCustomerId(customerId)
        .filter(credit -> isOverdue(credit))
        .hasElements(); // true si existe al menos 1
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
          // Actualizar saldo pendiente
          BigDecimal newBalance = credit.getOutstandingBalance().subtract(payment);
          if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO; // Evitar saldo negativo
          }
          credit.setOutstandingBalance(newBalance);

          // Actualizamos status si el saldo llegó a 0
          if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            credit.setStatus(RecordStatus.CLOSED);
          }
          return creditRepository.save(credit);
        })
        .map(creditMapper::entityToResponse);
  }

  /**
   * Valida si el crédito está en morosidad:
   * - Criterio simple: si dueDate ya pasó y outstandingBalance > 0 => overdue.
   */
  private boolean isOverdue(Credit credit) {
    if (credit.getDueDate() != null
        && credit.getDueDate().isBefore(java.time.LocalDate.now())
        && credit.getOutstandingBalance().compareTo(BigDecimal.ZERO) > 0) {
      return true;
    }
    return false;
  }

  /**
   * Valida las reglas antes de crear el crédito:
   * - Si es PERSONAL y subType=STANDARD, solo 1 crédito activo.
   * - Si el cliente tiene morosidad, rechazar.
   */
  private Mono<Void> validateBeforeCreating(Credit credit) {
    return getCustomerTypeAndSubType(credit.getCustomerId())
        .flatMap(tuple -> {
          String customerType = tuple.getT1();  // "PERSONAL", "ENTERPRISE"
          String subType = tuple.getT2();       // "STANDARD", "VIP", "PYME"

          // Verificar morosidad
          return hasOverdueDebt(credit.getCustomerId())
              .flatMap(hasOverdue -> {
                if (hasOverdue) {
                  return Mono.error(new ApiValidateException("Customer has overdue debt. Cannot create new credit."));
                }
                // Validar límite de crédito personal
                if (CreditType.PERSONAL == credit.getCreditType()
                    && "PERSONAL".equalsIgnoreCase(customerType)) {
                  return validatePersonalCreditLimit(credit.getCustomerId(), subType);
                }
                return Mono.empty();
              });
        });
  }

  /**
   * Método para obtener tipo y subtipo de cliente desde Microservicio de Clientes.
   */
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


  /**
   * Valida que el cliente personal estándar solo tenga 1 crédito personal activo.
   */
  private Mono<Void> validatePersonalCreditLimit(String customerId, String subType) {
    if ("STANDARD".equalsIgnoreCase(subType)) {
      return creditRepository.findByCustomerId(customerId)
          .filter(c -> c.getCreditType() == CreditType.PERSONAL
              && (c.getStatus() == RecordStatus.ACTIVE
              || c.getStatus() == RecordStatus.BLOCKED))
          .count()
          .flatMap(count -> {
            if (count >= 1) {
              return Mono.error(new ApiValidateException("STANDARD personal customer already has a personal credit."));
            }
            return Mono.empty();
          });
    }
    // Para VIP se podría permitir más de uno o la misma política. Ajustar según la regla real.
    return Mono.empty();
  }
}