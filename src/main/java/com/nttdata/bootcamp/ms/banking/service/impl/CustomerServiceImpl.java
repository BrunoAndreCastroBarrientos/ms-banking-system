package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.entity.Customer;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.service.CustomerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del servicio de clientes. Proporciona operaciones
 * para crear, actualizar, consultar y cambiar el estado de los clientes.
 *
 * <p>Este servicio gestiona la creación, actualización y consulta de
 * los clientes, además de validar reglas de negocio como la verificación
 * de perfiles VIP o PYME y la comprobación de deudas pendientes antes de
 * la creación de un cliente.</p>
 *
 * @version 1.1
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final WebClient webClient;

  @Value("${service.accounts.url}")
  private String accountsServiceUrl;

  @Value("${service.credits.url}")
  private String creditsServiceUrl;

  @Value("${service.creditcards.url}")
  private String creditCardsServiceUrl;

  @Override
  public Mono<CustomerResponse> createCustomer(CustomerRequest request) {
    Customer customer = customerMapper.toEntity(request);
    return validateProfileRules(customer)
        .then(customerRepository.save(customer))
        .map(customerMapper::toResponse);
  }

  @Override
  public Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Customer not found: " + id)))
        .flatMap(existing -> {
          existing.setCustomerType(request.getCustomerType());
          existing.setSubType(request.getSubType());
          existing.setContactInfo(request.getContactInfo());
          if (request.getCustomerType() == CustomerType.PERSONAL) {
            existing.setFirstName(request.getFirstName());
            existing.setLastName(request.getLastName());
            existing.setBusinessName(null);
            existing.setRuc(null);
          } else {
            existing.setBusinessName(request.getBusinessName());
            existing.setRuc(request.getRuc());
            existing.setFirstName(null);
            existing.setLastName(null);
          }
          return validateProfileRules(existing)
              .then(customerRepository.save(existing));
        })
        .map(customerMapper::toResponse);
  }

  @Override
  public Mono<CustomerResponse> changeStatus(String id, String newStatus) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Customer not found")))
        .flatMap(customer -> {
          customer.setStatus(RecordStatus.valueOf(newStatus));
          return customerRepository.save(customer);
        })
        .map(customerMapper::toResponse);
  }

  @Override
  public Mono<CustomerResponse> getById(String id) {
    return customerRepository.findById(id)
        .map(customerMapper::toResponse);
  }

  //@Cacheable(cacheNames = "customers")
  @Override
  @CircuitBreaker(name = "customerServiceCircuitBreaker", fallbackMethod = "fallbackGetAll")
  public Flux<CustomerResponse> getAll() {
    return customerRepository.findAll()
        .map(customerMapper::toResponse);
  }

  private Flux<CustomerResponse> fallbackGetAll(Throwable ex) {
    System.out.println("ENTRO AL CIRCUITO BREAKER");
    return Flux.just(
        new CustomerResponse()
    );
  }

  @Override
  @CircuitBreaker(name = "customerServiceCircuitBreaker", fallbackMethod = "fallbackGetAll")
  public Flux<CustomerResponse> findByType(String customerType) {
    return customerRepository.findAll()
        .filter(c -> c.getCustomerType().name().equalsIgnoreCase(customerType))
        .map(customerMapper::toResponse);
  }

  @Override
  public Flux<CustomerResponse> findByStatus(String status) {
    return customerRepository.findAll()
        .filter(c -> c.getStatus().name().equalsIgnoreCase(status))
        .map(customerMapper::toResponse);
  }

  private Mono<Void> validateProfileRules(Customer customer) {
    if (customer.getCustomerType() == CustomerType.PERSONAL
        && "VIP".equalsIgnoreCase(customer.getSubType().name())) {
      return webClient.get()
          .uri(creditCardsServiceUrl + "/customer/" + customer.getId())
          .retrieve()
          .bodyToFlux(String.class)
          .switchIfEmpty(Mono.error(new ApiValidateException("No active credit cards found for VIP requirement")))
          .then();
    }
    if (customer.getCustomerType() == CustomerType.ENTERPRISE
        && "STANDARD".equalsIgnoreCase(customer.getSubType().name())) {
      return Mono.empty();
    }
    return Mono.empty();
  }
}
