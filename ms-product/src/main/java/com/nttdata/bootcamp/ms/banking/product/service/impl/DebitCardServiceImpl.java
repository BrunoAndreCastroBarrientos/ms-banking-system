package com.nttdata.bootcamp.ms.banking.product.service.impl;

import com.nttdata.bootcamp.ms.banking.product.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.product.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.request.DebitCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.product.dto.response.DebitCardResponse;
import com.nttdata.bootcamp.ms.banking.product.entity.Customer;
import com.nttdata.bootcamp.ms.banking.product.entity.DebitCard;
import com.nttdata.bootcamp.ms.banking.product.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.product.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.product.mapper.DebitCardMapper;
import com.nttdata.bootcamp.ms.banking.product.repository.DebitCardRepository;
import com.nttdata.bootcamp.ms.banking.product.service.DebitCardService;
import com.nttdata.bootcamp.ms.banking.product.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@Service
@RequiredArgsConstructor
public class DebitCardServiceImpl implements DebitCardService {

  private final DebitCardRepository debitCardRepository;
  private final DebitCardMapper debitCardMapper;
  private final WebClient customerWebClient;
  private final CustomerMapper customerMapper;

  public Mono<DebitCardResponse> createDebitCard(DebitCardRequest request) {
    return this.findCustomerById(request.getCustomerId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(customer -> {
          DebitCard debitCard = debitCardMapper.toEntity(request);
          return debitCardRepository.save(debitCard)
              .map(debitCardMapper::toResponse);
        });
  }


  @Cacheable(value = "debitcard", key = "#id")
  public Mono<DebitCardResponse> getDebitCardById(String id) {
    return debitCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .map(debitCardMapper::toResponse);
  }

  @CachePut(value = "debitcard", key = "#id")
  public Mono<DebitCardResponse> updateDebitCard(String id, DebitCardRequest request) {
    return debitCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          DebitCard updated = debitCardMapper.toEntity(request);
          updated.setId(existing.getId());
          return debitCardRepository.save(updated);
        })
        .map(debitCardMapper::toResponse);
  }
  @CachePut(value = "debitcard", key = "#id")
  public Mono<Void> deleteDebitCard(String id) {
    debitCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          existing.setStatus(RecordStatus.INACTIVE);
          debitCardRepository.save(existing);
          return null;
        });
    return null;
  }

  private Mono<Customer> findCustomerById(String id) {
    return customerWebClient.get()
        .uri("/{id}", id)
        .retrieve()
        .bodyToMono(CustomerResponse.class)
        .map(customerMapper::responseToEntity)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")));
  }

  private Mono<Customer> saveCustomer(CustomerRequest request) {
    return customerWebClient.post()
        .bodyValue(request)
        .retrieve()
        .bodyToMono(CustomerResponse.class)
        .map(customerMapper::responseToEntity)
        .onErrorMap(WebClientResponseException.class,
            e -> new ApiValidateException("Failed to save account: " + e.getMessage())); // Manejo de errores
  }
}