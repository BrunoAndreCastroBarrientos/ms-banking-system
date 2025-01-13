package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.repository.CreditCardRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditCardService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Implementación del servicio de tarjetas de crédito.
 * Proporciona operaciones para crear, consultar,
 * actualizar el saldo y bloquear tarjetas de crédito.
 *
 * <p>Esta clase utiliza un repositorio de tarjetas de
 * crédito y un mapper para convertir entre objetos de
 * dominio y DTOs. Además, verifica si un cliente tiene
 * deudas pendientes antes de permitir la creación
 * de una nueva tarjeta.</p>
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

  private final CreditCardRepository creditCardRepository;
  private final CreditCardMapper creditCardMapper;
  private final WebClient webClient;

  @Value("${service.credit.url}")
  private String creditsServiceUrl;


  @Override
  public Mono<CreditCardResponse> createCreditCard(CreditCardRequest request) {
    return verifyNoOverdueDebt(request.getCustomerId())
        .flatMap(hasDebt -> {
          if (Boolean.TRUE.equals(hasDebt)) {
            return Mono.error(new ApiValidateException(
                "El cliente tiene deuda vencida. No se puede emitir nueva tarjeta de crédito."));
          }
          // Podrías añadir validaciones: si es VIP/PYME, etc.
          CreditCard card = creditCardMapper.toEntity(request);
          return creditCardRepository.save(card);
        })
        .map(creditCardMapper::toResponse);
  }

  @Override
  public Mono<CreditCardResponse> getById(String cardId) {
    return creditCardRepository.findById(cardId)
        .map(creditCardMapper::toResponse);
  }

  @Override
  public Flux<CreditCardResponse> getByCustomerId(String customerId) {
    return creditCardRepository.findByCustomerId(customerId)
        .filter(card -> "CREDIT".equalsIgnoreCase(card.getType()))
        .map(creditCardMapper::toResponse);
  }

  @Override
  public Mono<CreditCardResponse> updateBalance(String cardId, double amount, boolean isPayment) {
    return creditCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Tarjeta de crédito no encontrada.")))
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
              return Mono.error(new ApiValidateException("Límite de crédito insuficiente."));
            }
            card.setBalance(card.getBalance().add(amt));
            card.setAvailableLimit(card.getAvailableLimit().subtract(amt));
          }
          return creditCardRepository.save(card);
        })
        .map(creditCardMapper::toResponse);
  }

  @Override
  public Mono<CreditCardResponse> blockCard(String cardId) {
    return creditCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new ApiValidateException("Tarjeta de crédito no encontrada.")))
        .flatMap(card -> {
          card.setStatus(RecordStatus.BLOCKED);
          return creditCardRepository.save(card);
        })
        .map(creditCardMapper::toResponse);
  }

  private Mono<Boolean> verifyNoOverdueDebt(String customerId) {
    return webClient.get()
        .uri(creditsServiceUrl + "/debts/pending/{customerId}", customerId)
        .retrieve()
        .bodyToMono(Boolean.class)
        .defaultIfEmpty(false);
  }
}