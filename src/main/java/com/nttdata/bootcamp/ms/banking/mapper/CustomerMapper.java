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

  public Customer requestToEntity(CustomerRequest request) {
    Customer customer = new Customer();
    customer.setCustomerType(CustomerType.valueOf(request.getCustomerType()));
    customer.setSubType(CustomerProfile.valueOf(request.getSubType()));
    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setBusinessName(request.getBusinessName());
    customer.setRuc(request.getRuc());
    customer.setContactInfo(request.getContactInfo());
    customer.setCreationDate(LocalDateTime.now());
    customer.setStatus(RecordStatus.ACTIVE);
    return customer;
  }

  public CustomerResponse entityToResponse(Customer customer) {
    CustomerResponse response = new CustomerResponse();
    response.setId(customer.getId());
    response.setCustomerType(customer.getCustomerType().name());
    response.setSubType(customer.getSubType().name());
    response.setFirstName(customer.getFirstName());
    response.setLastName(customer.getLastName());
    response.setBusinessName(customer.getBusinessName());
    response.setRuc(customer.getRuc());
    response.setContactInfo(customer.getContactInfo());
    response.setCreationDate(customer.getCreationDate());
    response.setStatus(customer.getStatus().name());
    return response;
  }
}
