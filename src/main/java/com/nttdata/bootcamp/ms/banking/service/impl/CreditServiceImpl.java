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
        return Mono.create(sink -> {
            // Verificar si ya existe un crédito para el cliente
            creditRepository.findByClientId(clientId)
                    .doOnTerminate(() -> sink.success()) // Se garantiza que la creación solo se realice si no existe un crédito
                    .flatMap(existingCredit -> {
                        // Lanza un error si el cliente ya tiene un crédito
                        sink.error(new ApiValidateException("Un cliente personal solo puede tener un crédito"));
                        return Mono.empty(); // Si el cliente ya tiene un crédito, no hace nada más
                    })
                    .switchIfEmpty(
                            // Si no existe crédito, se crea el nuevo crédito
                            creditRepository.save(creditRequest.toEntity())  // Guardar el crédito creado
                                    .map(CreditResponse::fromEntity)  // Convertir la entidad a CreditResponse
                                    .doOnSuccess(sink::success) // Llama success con el CreditResponse
                                    .doOnError(sink::error) // Llama error en caso de fallo
                    );
        });
    }

    private Mono<CreditResponse> validateBusinessCredit(String clientId, CreditRequest creditRequest) {
        return creditRepository.save(creditRequest.toEntity())
                .map(CreditResponse::fromEntity);  // Convertir la entidad a CreditResponse
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
                .flatMap(credit -> creditRepository.delete(credit))
                .switchIfEmpty(Mono.error(new ApiValidateException("Crédito no encontrado")));
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
