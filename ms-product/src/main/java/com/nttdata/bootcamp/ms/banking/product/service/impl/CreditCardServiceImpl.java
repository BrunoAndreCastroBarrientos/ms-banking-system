package com.nttdata.bootcamp.ms.banking.product.service.impl;

import com.nttdata.bootcamp.ms.banking.product.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.product.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.product.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.product.entity.Customer;
import com.nttdata.bootcamp.ms.banking.product.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.product.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.product.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.product.repository.CreditCardRepository;
import com.nttdata.bootcamp.ms.banking.product.service.CreditCardService;
import com.nttdata.bootcamp.ms.banking.product.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
  private final WebClient customerWebClient;
  private final CustomerMapper customerMapper;

  public Mono<CreditCardResponse> createCreditCard(CreditCardRequest request) {
    return this.findCustomerById(request.getCustomerId()) // Busca el cliente
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE))) // Lanza error si no existe
        .flatMap(customer -> {
          CreditCard creditCard = creditCardMapper.toEntity(request);
          return creditCardRepository.save(creditCard)
              .map(creditCardMapper::toResponse);
        });
  }

  @Cacheable(value = "creditcard", key = "#id")
  public Mono<CreditCardResponse> getCreditCardById(String id) {
    return creditCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .map(creditCardMapper::toResponse);
  }

  @CachePut(value = "creditcard", key = "#id")
  public Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest request) {
    return creditCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          CreditCard updated = creditCardMapper.toEntity(request);
          updated.setId(existing.getId());
          return creditCardRepository.save(updated);
        })
        .map(creditCardMapper::toResponse);
  }

  @CachePut(value = "creditcard", key = "#id")
  public Mono<Void> deleteCreditCard(String id) {
    creditCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          existing.setStatus(RecordStatus.INACTIVE);
          creditCardRepository.save(existing);
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