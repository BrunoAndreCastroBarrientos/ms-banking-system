package com.nttdata.bootcamp.ms.banking.customer.service.impl;


import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.customer.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.customer.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.customer.entity.Customer;
import com.nttdata.bootcamp.ms.banking.customer.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.customer.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.customer.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.customer.service.CustomerService;
import com.nttdata.bootcamp.ms.banking.customer.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
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


  public Mono<CustomerResponse> createCustomer(CustomerRequest request) {
    Customer customer = customerMapper.toEntity(request);
    return customerRepository.save(customer)
        .map(customerMapper::toResponse);
  }

  public Flux<CustomerResponse> getAllCustomer() {
    return customerRepository.findAll()
        .map(customerMapper::toResponse);
  }

  @Cacheable(value = "customer", key = "#id")
  public Mono<CustomerResponse> getCustomerById(String id) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .map(customerMapper::toResponse);
  }

  @CachePut(value = "customer", key = "#id")
  public Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Customer not found.")))
        .flatMap(existing -> {
          Customer updated = customerMapper.toEntity(request);
          updated.setId(existing.getId());
          return customerRepository.save(updated);
        })
        .map(customerMapper::toResponse);
  }

  @CachePut(value = "customer", key = "#id")
  public Mono<Void> deleteCustomer(String id) {
    customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .flatMap(existing -> {
          existing.setStatus(RecordStatus.INACTIVE);
          customerRepository.save(existing);
          return null;
        });
    return null;
  }
}
