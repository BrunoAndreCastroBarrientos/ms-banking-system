package com.nttdata.bootcamp.ms.banking.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.CreditType;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CreditMapper;
import com.nttdata.bootcamp.ms.banking.repository.CreditRepository;
import com.nttdata.bootcamp.ms.banking.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditService;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

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
  private final CustomerRepository customerRepository;

  public Mono<CreditResponse> createCredit(CreditRequest request) {
    return customerRepository.findById(request.getCustomerId())
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
}
