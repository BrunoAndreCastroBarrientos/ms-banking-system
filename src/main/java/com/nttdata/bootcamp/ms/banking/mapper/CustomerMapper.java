package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.Customer;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerProfile;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CustomerType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CustomerResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
}
