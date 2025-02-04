package com.nttdata.bootcamp.ms.banking.product.service.impl;

import com.nttdata.bootcamp.ms.banking.product.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.product.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.product.entity.Credit;
import com.nttdata.bootcamp.ms.banking.product.entity.Customer;
import com.nttdata.bootcamp.ms.banking.product.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.product.mapper.CreditMapper;
import com.nttdata.bootcamp.ms.banking.product.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.product.repository.CreditRepository;
import com.nttdata.bootcamp.ms.banking.product.service.CreditService;
import com.nttdata.bootcamp.ms.banking.product.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
  private final WebClient customerWebClient;
  private final CustomerMapper customerMapper;

  public Mono<CreditResponse> createCredit(CreditRequest request) {
    return this.findCustomerById(request.getCustomerId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(customer -> {
          Credit credit = creditMapper.toEntity(request);
          credit.setDebt(request.getAmount()
              .add(request.getAmount().multiply(request.getInterestRate().divide(BigDecimal.valueOf(100))))
              .setScale(2, RoundingMode.HALF_UP));
          return creditRepository.save(credit)
              .map(creditMapper::toResponse);
        });
  }


  @Cacheable(value = "credit", key = "#id")
  public Mono<CreditResponse> getCreditById(String id) {
    return creditRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .map(creditMapper::toResponse);
  }

  @CachePut(value = "credit", key = "#id")
  public Mono<CreditResponse> updateCredit(String id, CreditRequest request) {
    return creditRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          Credit updated = creditMapper.toEntity(request);
          updated.setId(existing.getId());
          return creditRepository.save(updated);
        })
        .map(creditMapper::toResponse);
  }

  @CachePut(value = "credit", key = "#id")
  public Mono<Void> deleteCredit(String id) {
    return creditRepository.deleteById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)));
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
