package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.DebitCard;
import com.nttdata.bootcamp.ms.banking.mapper.DebitCardMapper;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.repository.DebitCardRepository;
import com.nttdata.bootcamp.ms.banking.service.DebitCardService;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio de tarjetas de débito.
 * Proporciona operaciones para crear, consultar,
 * bloquear y realizar retiros con tarjetas de débito.
 *
 * <p>Este servicio gestiona la creación y el estado
 * de las tarjetas de débito, así como la lógica
 * de los retiros de las cuentas asociadas a la tarjeta,
 * gestionando las operaciones en cascada
 * cuando no se dispone de suficiente saldo en la cuenta
 * principal.</p>
 *
 * <p>También permite consultar los últimos movimientos
 * de la tarjeta de débito y bloquear una
 * tarjeta.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */
@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {

  private final DebitCardRepository debitCardRepository;
  private final DebitCardMapper debitCardMapper;
  private final WebClient webClient;

  @Value("${service.transaction.url}")
  private String transactionsServiceUrl;
  @Value("${service.account.url}")
  private String accountsServiceUrl;

  @Override
  public Mono<DebitCardResponse> createDebitCard(DebitCardRequest request) {
    DebitCard card = debitCardMapper.toEntity(request);
    card.setType("DEBIT");
    card.setStatus(RecordStatus.ACTIVE);
    return debitCardRepository.save(card)
        .map(debitCardMapper::toResponse);
  }

  @Override
  public Mono<DebitCardResponse> getById(String cardId) {
    return debitCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new RuntimeException("Tarjeta de débito no encontrada.")))
        .map(debitCardMapper::toResponse);
  }

  @Override
  public Flux<DebitCardResponse> getByCustomerId(String customerId) {
    return debitCardRepository.findByCustomerId(customerId)
        .filter(card -> "DEBIT".equalsIgnoreCase(card.getType()))
        .map(debitCardMapper::toResponse);
  }

  @Override
  public Mono<Void> withdraw(String cardId, double amount) {
    return debitCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new RuntimeException("Tarjeta de débito no encontrada.")))
        .flatMap(card -> performCascadeWithdrawal(card, amount));
  }

  private Mono<Void> performCascadeWithdrawal(DebitCard card, double amount) {
    List<String> accounts = card.getAssociatedAccounts();
    return debitInOrder(accounts, amount).then();
  }

  private Mono<Double> debitInOrder(List<String> accounts, double amount) {
    if (accounts.isEmpty() || amount <= 0) {
      return Mono.just(amount);
    }
    String accountId = accounts.get(0);
    return webClient.patch()
        .uri(accountsServiceUrl + "/{accountId}/debit?amount={amt}", accountId, amount)
        .retrieve()
        .bodyToMono(BigDecimal.class)
        .map(BigDecimal::doubleValue)
        .flatMap(remaining -> {
          if (remaining <= 0) {
            return Mono.just(0.0);
          }
          return debitInOrder(accounts.subList(1, accounts.size()), remaining);
        });
  }


  @Override
  public Mono<DebitCardResponse> blockCard(String cardId) {
    return debitCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new RuntimeException("Tarjeta de débito no encontrada.")))
        .flatMap(card -> {
          card.setStatus(RecordStatus.BLOCKED);
          return debitCardRepository.save(card);
        })
        .map(debitCardMapper::toResponse);
  }

  @Override
  public Flux<String> getLastMovements(String cardId, int limit) {
    return webClient.get()
        .uri( transactionsServiceUrl + "/debitCard/{cardId}?limit={limit}", cardId, limit)
        .retrieve()
        .bodyToFlux(String.class);
  }
}