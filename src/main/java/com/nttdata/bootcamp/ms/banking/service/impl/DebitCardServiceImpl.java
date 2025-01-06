package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.DebitCard;
import com.nttdata.bootcamp.ms.banking.mapper.DebitCardMapper;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.repository.DebitCardRepository;
import com.nttdata.bootcamp.ms.banking.service.DebitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {

  private final DebitCardRepository debitCardRepository;
  private final DebitCardMapper debitCardMapper;

  // WebClient para llamadas a otros microservicios
  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<DebitCardResponse> createDebitCard(DebitCardRequest request) {
    // Validar si el cliente está bloqueado, si corresponde (Microservicio de Clientes).
    // También podrías verificar si la cuenta principal existe y le pertenece.
    DebitCard card = debitCardMapper.requestToEntity(request);
    return debitCardRepository.save(card)
        .map(debitCardMapper::entityToResponse);
  }

  @Override
  public Mono<DebitCardResponse> getById(String cardId) {
    return debitCardRepository.findById(cardId)
        .map(debitCardMapper::entityToResponse);
  }

  @Override
  public Flux<DebitCardResponse> getByCustomerId(String customerId) {
    return debitCardRepository.findByCustomerId(customerId)
        .filter(card -> "DEBIT".equalsIgnoreCase(card.getType()))
        .map(debitCardMapper::entityToResponse);
  }

  /**
   * Lógica de retiro:
   * - Se descuenta de la cuenta principal.
   * - Si no hay suficiente saldo, se intenta con las cuentas secundarias en orden.
   * - Aquí se hacen llamadas al microservicio de Cuentas para debitar cada cuenta.
   */
  @Override
  public Mono<Void> withdraw(String cardId, double amount) {
    return debitCardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new RuntimeException("Tarjeta de débito no encontrada.")))
        .flatMap(card -> performCascadeWithdrawal(card, amount));
  }

  private Mono<Void> performCascadeWithdrawal(DebitCard card, double amount) {
    // 1. Tomar la lista de cuentas asociadas, la primera es la 'primaryAccount' o ya se define en el Request.
    // 2. Llamar al microservicio de Cuentas para debitar saldo.
    // 3. Si no alcanza, pasar a la siguiente, y así sucesivamente.
    // 4. Finalmente, si no fue posible cubrir el total, arrojar error o quedar en partial?
    List<String> accounts = card.getAssociatedAccounts();

    // Ejemplo muy simplificado:
    return debitInOrder(accounts, amount)
        .then();
  }

  /**
   * Ejemplo de método recursivo o iterativo para descontar en cascada.
   * Aquí se omite la implementación real; dependería de la API del microservicio de Cuentas.
   */
  private Mono<Double> debitInOrder(List<String> accounts, double amount) {
    if (accounts.isEmpty() || amount <= 0) {
      return Mono.just(amount);
    }
    String accountId = accounts.get(0);
    // Supón que tenemos un endpoint: POST /accounts/{accountId}/debit?amount=xxx
    return webClient.post()
        .uri("http://ACCOUNTS-SERVICE/api/accounts/{accountId}/debit?amount={amt}",
            accountId, amount)
        .retrieve()
        .bodyToMono(Double.class)  // Imaginemos que retorna el monto restante si no cubrió todo, o 0 si cubrió
        .flatMap(remaining -> {
          if (remaining <= 0) {
            // Se cubrió todo el retiro en esta cuenta
            return Mono.just(0.0);
          }
          // Aún queda monto por cubrir, pasar a la siguiente cuenta
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
        .map(debitCardMapper::entityToResponse);
  }

  /**
   * Consultar los últimos N movimientos (p.ej. 10). Aquí llamamos al microservicio
   * de Transacciones, que podría tener un endpoint GET /transactions/debitCard/{cardId}?limit=N
   */
  @Override
  public Flux<String> getLastMovements(String cardId, int limit) {
    // Ejemplo: se asume que el microservicio de Transacciones retorna una lista de strings con la descripción
    return webClient.get()
        .uri("http://TRANSACTIONS-SERVICE/api/transactions/debitCard/{cardId}?limit={limit}", cardId, limit)
        .retrieve()
        .bodyToFlux(String.class);
  }
}