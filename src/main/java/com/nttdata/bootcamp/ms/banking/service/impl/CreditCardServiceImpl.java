package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.repository.CreditCardRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

  private final CreditCardRepository creditCardRepository;
  private final CreditCardMapper creditCardMapper;

  // Ejemplo de WebClient (podrías inyectarlo con @Bean o usar Spring Cloud)
  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<CreditCardResponse> createCreditCard(CreditCardRequest request) {
    return verifyNoOverdueDebt(request.getCustomerId())
        .flatMap(hasDebt -> {
          if (Boolean.TRUE.equals(hasDebt)) {
            return Mono.error(new RuntimeException(
                "El cliente tiene deuda vencida. No se puede emitir nueva tarjeta de crédito."));
          }
          // Podrías añadir validaciones: si es VIP/PYME, etc.
          CreditCard card = creditCardMapper.requestToEntity(request);
          return creditCardRepository.save(card);
        })
        .map(creditCardMapper::entityToResponse);
  }

  @Override
  public Mono<CreditCardResponse> getById(String cardId) {
    return creditCardRepository.findById(cardId)
        .map(creditCardMapper::entityToResponse);
  }

  @Override
  public Flux<CreditCardResponse> getByCustomerId(String customerId) {
    return creditCardRepository.findByCustomerId(customerId)
        .filter(card -> "CREDIT".equalsIgnoreCase(card.getType()))
        .map(creditCardMapper::entityToResponse);
  }

  /**
   * Actualiza la tarjeta al realizar un consumo o un pago:
   * - isPayment=true => se incrementa el availableLimit y se reduce el balance.
   * - isPayment=false => se reduce el availableLimit y se incrementa el balance.
   */
  @Override
  public Mono<CreditCardResponse> updateBalance(String cardId, double amount, boolean isPayment) {
    return creditCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new RuntimeException("Tarjeta de crédito no encontrada.")))
        .flatMap(card -> {
          BigDecimal amt = BigDecimal.valueOf(amount);
          if (isPayment) {
            // Pago
            card.setBalance(card.getBalance().subtract(amt));
            if (card.getBalance().compareTo(BigDecimal.ZERO) < 0) {
              card.setBalance(BigDecimal.ZERO);
            }
            card.setAvailableLimit(
                card.getAvailableLimit().add(amt).min(card.getCreditLimit())
            );
          } else {
            // Consumo
            if (card.getAvailableLimit().compareTo(amt) < 0) {
              return Mono.error(new RuntimeException("Límite de crédito insuficiente."));
            }
            card.setBalance(card.getBalance().add(amt));
            card.setAvailableLimit(card.getAvailableLimit().subtract(amt));
          }
          return creditCardRepository.save(card);
        })
        .map(creditCardMapper::entityToResponse);
  }

  @Override
  public Mono<CreditCardResponse> blockCard(String cardId) {
    return creditCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new RuntimeException("Tarjeta de crédito no encontrada.")))
        .flatMap(card -> {
          card.setStatus(RecordStatus.BLOCKED);
          return creditCardRepository.save(card);
        })
        .map(creditCardMapper::entityToResponse);
  }

  /**
   * Ejemplo: Llamada al microservicio de Créditos para verificar si el cliente
   * tiene deuda vencida (true/false).
   */
  private Mono<Boolean> verifyNoOverdueDebt(String customerId) {
    return webClient.get()
        .uri("http://CREDIT-SERVICE/api/credits/debts/pending/{customerId}", customerId)
        .retrieve()
        .bodyToMono(Boolean.class)
        .defaultIfEmpty(false);
  }
}