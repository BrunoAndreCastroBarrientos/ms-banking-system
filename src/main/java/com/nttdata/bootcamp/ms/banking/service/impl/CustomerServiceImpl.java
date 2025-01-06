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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
 * @author Bruno Andre Castro Barrientos
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;

  // Ejemplos de URLs de otros microservicios (Cuentas, Créditos, Tarjetas).
  @Value("${service.accounts.url}")
  private String accountsServiceUrl;
  @Value("${service.credits.url}")
  private String creditsServiceUrl;
  @Value("${service.cards.url}")
  private String cardsServiceUrl;

  private final WebClient webClient = WebClient.create();

  @Override
  public Mono<CustomerResponse> createCustomer(CustomerRequest request) {
    // 1. Convertimos el request a entidad
    Customer customer = customerMapper.requestToEntity(request);

    // 2. Validaciones de negocio (ilustración)
    return validateProfileRules(customer)
        .then(checkPendingDebts(customer.getId()))
        .then(customerRepository.save(customer))
        .map(saved -> customerMapper.entityToResponse(saved));
  }

  @Override
  public Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Customer not found: " + id)))
        .flatMap(existing -> {
          existing.setCustomerType(request.getCustomerType());
          return validateProfileRules(existing)
              .then(customerRepository.save(existing));
        })
        .map(customerMapper::entityToResponse);
  }

  @Override
  public Mono<CustomerResponse> changeStatus(String id, String newStatus) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Customer not found")))
        .flatMap(customer -> {
          customer.setStatus(RecordStatus.valueOf(newStatus));
          return customerRepository.save(customer);
        })
        .map(customerMapper::entityToResponse);
  }

  @Override
  public Mono<CustomerResponse> getById(String id) {
    return customerRepository.findById(id)
        .map(customerMapper::entityToResponse);
  }

  @Override
  public Flux<CustomerResponse> getAll() {
    return customerRepository.findAll()
        .map(customerMapper::entityToResponse);
  }

  @Override
  public Flux<CustomerResponse> findByType(String customerType) {
    // Ejemplo: filtrar en memoria o crear método en el repositorio
    return customerRepository.findAll()
        .filter(c -> c.getCustomerType().name().equals(customerType))
        .map(customerMapper::entityToResponse);
  }

  @Override
  public Flux<CustomerResponse> findByStatus(String status) {
    // Ejemplo simple. Podríamos tener un findByStatus en el repositorio.
    return customerRepository.findAll()
        .filter(c -> c.getStatus().name().equals(status))
        .map(customerMapper::entityToResponse);
  }

  /**
   * Ejemplo de validación de perfil (VIP, PYME, etc.).
   * Se podrían hacer llamadas a microservicio de cuentas o tarjetas para verificar requisitos.
   */
  private Mono<Void> validateProfileRules(Customer customer) {
    // Aquí se ilustran reglas de negocio de manera simple.
    // Por ejemplo, si es PERSONAL VIP -> verificar al menos una tarjeta de crédito (uso ficticio de WebClient).
    if (customer.getCustomerType() == CustomerType.PERSONAL
        && "VIP".equalsIgnoreCase(customer.getSubType().name())) {

      // Llamamos al microservicio de tarjetas para ver si tiene una tarjeta activa:
      return webClient.get()
          .uri(cardsServiceUrl + "/cards/customer/" + customer.getId() + "/active")
          .retrieve()
          .bodyToFlux(String.class) // Ejemplo: podría devolver IDs de tarjetas
          .switchIfEmpty(Mono.error(new ApiValidateException("No active credit cards found for VIP requirement")))
          .then();
    }

    // Regla: si es EMPRESARIAL STANDARD -> no puede tener cuentas de ahorro/plazo fijo, etc.
    // Este tipo de validaciones se podrían hacer en 'create account' en el microservicio de Cuentas,
    // pero a veces hay validaciones cruzadas en el alta del cliente (depende del negocio).

    return Mono.empty(); // Sin errores => validación OK
  }

  /**
   * Ejemplo de verificación de deuda vencida en microservicio de Créditos.
   */
  private Mono<Void> checkPendingDebts(String customerId) {
    return webClient.get()
        .uri(creditsServiceUrl + "/credits/debts/pending/" + customerId)
        .retrieve()
        .bodyToMono(Boolean.class) // true => tiene deuda vencida, false => sin deuda
        .flatMap(hasDebt -> {
          if (Boolean.TRUE.equals(hasDebt)) {
            return Mono.error(new ApiValidateException("El cliente posee deuda vencida. Operación rechazada."));
          }
          return Mono.empty();
        });
  }
}
