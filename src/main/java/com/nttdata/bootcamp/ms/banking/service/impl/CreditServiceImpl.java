package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.model.ClientType;
import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.model.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.repository.ClientRepository;
import com.nttdata.bootcamp.ms.banking.repository.CreditRepository;
import com.nttdata.bootcamp.ms.banking.service.ClientService;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;

    @Override
    public Mono<CreditResponse> createCredit(CreditRequest creditRequest) {
        return clientRepository.findById(creditRequest.getClientId())
                .flatMap(client -> {
                    if (client.getType() == ClientType.PERSONAL) {
                        return validatePersonalCredit(client.getId(), creditRequest);
                    } else if (client.getType() == ClientType.EMPRESARIAL) {
                        return validateBusinessCredit(client.getId(), creditRequest);
                    } else {
                        return Mono.error(new ApiValidateException("Tipo de cliente no permitido"));
                    }
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cliente no encontrado")));
    }

    public Mono<CreditResponse> validatePersonalCredit(String clientId, CreditRequest creditRequest) {
        return clientRepository.findById(clientId) // Consultar los datos del cliente por ID
                .switchIfEmpty(Mono.error(new ApiValidateException("El cliente no existe.")))
                .flatMap(cliente -> {
                    if (!cliente.getType().equals(ClientType.PERSONAL)) {
                        return Mono.error(new ApiValidateException("El cliente no es de tipo personal."));
                    }
                    // Validar si el cliente personal ya tiene un crédito
                    return creditRepository.findByClientId(clientId)
                            .hasElements()
                            .flatMap(hasCredit -> {
                                if (hasCredit) {
                                    return Mono.error(new ApiValidateException("Un cliente personal solo puede tener un crédito activo."));
                                }
                                // Crear el crédito si no existe uno previo
                                Credit nuevoCredito = creditRequest.toEntity();
                                nuevoCredito.setClientId(clientId);
                                return creditRepository.save(nuevoCredito)
                                        .map(CreditResponse::fromEntity);
                            });
                });
    }

    private Mono<CreditResponse> validateBusinessCredit(String clientId, CreditRequest creditRequest) {
        return clientRepository.findById(clientId) // Consultar los datos del cliente por ID
                .switchIfEmpty(Mono.error(new ApiValidateException("El cliente no existe.")))
                .flatMap(cliente -> {
                    if (!cliente.getType().equals(ClientType.EMPRESARIAL)) {
                        return Mono.error(new ApiValidateException("El cliente no es de tipo empresarial."));
                    }
                    // Consultar todos los créditos del cliente empresarial
                    return creditRepository.findByClientId(clientId)
                            .collectList()
                            .flatMap(existingCredits -> {
                                // Calcular el monto total de los créditos existentes
                                BigDecimal totalCredits = existingCredits.stream()
                                        .map(Credit::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                                BigDecimal creditLimit = new BigDecimal("1000000"); // Límite de crédito empresarial
                                if (totalCredits.add(creditRequest.getAmount()).compareTo(creditLimit) > 0) {
                                    return Mono.error(new ApiValidateException("El cliente empresarial no puede superar el límite de crédito total de " + creditLimit));
                                }
                                // Crear el nuevo crédito
                                Credit nuevoCredito = creditRequest.toEntity();
                                nuevoCredito.setClientId(clientId);
                                return creditRepository.save(nuevoCredito)
                                        .map(CreditResponse::fromEntity);
                            });
                });
    }


    @Override
    public Mono<CreditResponse> updateCredit(String creditId, CreditRequest creditRequest) {
        return creditRepository.findById(creditId)
                .flatMap(existingCredit -> {
                    // Actualización de los campos que existen en CreditRequest
                    existingCredit.setAmount(creditRequest.getAmount());
                    existingCredit.setInterestRate(creditRequest.getInterestRate());
                    existingCredit.setStatus(creditRequest.getStatus());
                    existingCredit.setCreditType(creditRequest.getCreditType());
                    existingCredit.setRemainingBalance(creditRequest.getRemainingBalance());
                    existingCredit.setUpdatedAt(LocalDateTime.now());

                    // Guardamos el crédito actualizado
                    return creditRepository.save(existingCredit)
                            .map(CreditResponse::fromEntity);  // Convertir la entidad a CreditResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Crédito no encontrado")));
    }



    @Override
    public Mono<Void> deleteCredit(String creditId) {
        return creditRepository.findById(creditId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Lo sentimos, crédito no encontrado"))) // Lanza error si no se encuentra
                .flatMap(credit ->
                        creditRepository.delete(credit) // Elimina el crédito
                                .then(Mono.empty())
                );
    }


    @Override
    public Mono<CreditResponse> getCreditById(String creditId) {
        return creditRepository.findById(creditId)
                .map(CreditResponse::fromEntity)  // Convertir la entidad a CreditResponse
                .switchIfEmpty(Mono.error(new ApiValidateException("Crédito no encontrado")));
    }

    @Override
    public Flux<CreditResponse> getCreditsByClientId(String clientId) {
        return creditRepository.findByClientId(clientId)
                .map(CreditResponse::fromEntity);  // Convertir la entidad a CreditResponse
    }
}
