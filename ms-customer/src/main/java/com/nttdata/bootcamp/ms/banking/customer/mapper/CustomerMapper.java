package com.nttdata.bootcamp.ms.banking.customer.mapper;

import com.nttdata.bootcamp.ms.banking.customer.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.customer.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.customer.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.customer.entity.Customer;
import com.nttdata.bootcamp.ms.banking.customer.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.customer.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.customer.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.customer.service.CustomerService;
import com.nttdata.bootcamp.ms.banking.customer.utility.ConstantUtil;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

  public Customer toEntity(CustomerRequest request) {
    Customer customer = new Customer();
    customer.setCustomerType(request.getCustomerType());
    customer.setSubType(request.getSubType());
    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setBusinessName(request.getBusinessName());
    customer.setIdentificationNumber(request.getIdentificationNumber());
    customer.setIdentificationType(request.getIdentificationType());
    customer.setEmail(request.getEmail());
    customer.setPhoneNumber(request.getPhoneNumber());
    customer.setCreationDate(request.getCreationDate());
    customer.setStatus(RecordStatus.valueOf("ACTIVE"));
    return customer;
  }

  public CustomerResponse toResponse(Customer customer) {
    CustomerResponse response = new CustomerResponse();
    response.setId(customer.getId());
    response.setCustomerType(customer.getCustomerType());
    response.setSubType(customer.getSubType());
    response.setFirstName(customer.getFirstName());
    response.setLastName(customer.getLastName());
    response.setBusinessName(customer.getBusinessName());
    response.setIdentificationNumber(customer.getIdentificationNumber());
    response.setIdentificationType(customer.getIdentificationType());
    response.setEmail(customer.getEmail());
    response.setPhoneNumber(customer.getPhoneNumber());
    response.setCreationDate(customer.getCreationDate());
    response.setStatus(customer.getStatus());
    return response;
  }

  public Customer responseToEntity(CustomerResponse response) {
    Customer customer = new Customer();
    customer.setId(response.getId());
    customer.setCustomerType(response.getCustomerType());
    customer.setSubType(response.getSubType());
    customer.setFirstName(response.getFirstName());
    customer.setLastName(response.getLastName());
    customer.setBusinessName(response.getBusinessName());
    customer.setIdentificationNumber(response.getIdentificationNumber());
    customer.setIdentificationType(response.getIdentificationType());
    customer.setEmail(response.getEmail());
    customer.setPhoneNumber(response.getPhoneNumber());
    customer.setCreationDate(response.getCreationDate());
    customer.setStatus(response.getStatus());
    return customer;
  }

  public CustomerRequest entityToRequest(Customer customer) {
    CustomerRequest request = new CustomerRequest();
    request.setCustomerType(customer.getCustomerType());
    request.setSubType(customer.getSubType());
    request.setFirstName(customer.getFirstName());
    request.setLastName(customer.getLastName());
    request.setBusinessName(customer.getBusinessName());
    request.setIdentificationNumber(customer.getIdentificationNumber());
    request.setIdentificationType(customer.getIdentificationType());
    request.setEmail(customer.getEmail());
    request.setPhoneNumber(customer.getPhoneNumber());
    request.setCreationDate(customer.getCreationDate());
    return request;
  }
}
