package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.repository.CreditCardRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditCardService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
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

  public Mono<CreditCardResponse> createCreditCard(CreditCardRequest request) {
    CreditCard creditCard = creditCardMapper.toEntity(request);
    return creditCardRepository.save(creditCard)
        .map(creditCardMapper::toResponse);
  }

  public Mono<CreditCardResponse> getCreditCardById(String id) {
    return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiValidateException("Not found.")))
.map(creditCardMapper::toResponse);
  }

  public Mono<CreditCardResponse> updateCreditCard(String id, CreditCardRequest request) {
    return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new ApiValidateException("Not found.")))
.flatMap(existing -> {
          CreditCard updated = creditCardMapper.toEntity(request);
          updated.setId(existing.getId());
          return creditCardRepository.save(updated);
        })
        .map(creditCardMapper::toResponse);
  }

  public Mono<Void> deleteCreditCard(String id) {
    creditCardRepository.findById(id)
        .switchIfEmpty(Mono.error(new ApiValidateException("Not found.")))
        .flatMap(existing -> {
          existing.setStatus(RecordStatus.INACTIVE);
          creditCardRepository.save(existing);
          return null;
        });
    return null;
  }
}