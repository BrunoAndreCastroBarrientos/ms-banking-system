package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.response.ClientResponse;
import com.nttdata.bootcamp.ms.banking.repository.ClientRepository;
import com.nttdata.bootcamp.ms.banking.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Mono<ClientResponse> createClient(ClientRequest request) {
        return validateClientRequest(request)
                .flatMap(this::checkExistingEmail)
                .flatMap(validatedRequest -> clientRepository.save(validatedRequest.toEntity()))
                .doOnSuccess(client -> log.info("Client created successfully: {}", client.getId()))
                .doOnError(ex -> log.error("Error creating client: {}", ex.getMessage()))
                .map(ClientResponse::fromEntity);
    }

    private Mono<ClientRequest> validateClientRequest(ClientRequest request) {
        return Mono.just(request)
                .filter(req -> req.getEmail() != null && !req.getEmail().trim().isEmpty())
                .filter(req -> req.getFirstName() != null && !req.getFirstName().trim().isEmpty())
                .filter(req -> req.getLastName() != null && !req.getLastName().trim().isEmpty())
                .filter(req -> req.getType() != null)
                .switchIfEmpty(Mono.error(new ApiValidateException("Invalid client data")));
    }

    private Mono<ClientRequest> checkExistingEmail(ClientRequest request) {
        return clientRepository.findByEmail(request.getEmail())
                .flatMap(existing -> Mono.error(new ApiValidateException("Email already registered")))
                .thenReturn(request);
    }

    @Override
    public Mono<ClientResponse> getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new ApiValidateException("Client not found")))
                .map(ClientResponse::fromEntity);
    }

    @Override
    public Mono<ClientResponse> getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Client not found")))
                .map(ClientResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteClientById(String clientId) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Client not found")))
                .flatMap(client -> clientRepository.deleteById(clientId));
    }

    @Override
    public Mono<ClientResponse> updateClient(String clientId, ClientRequest request) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Client not found")))
                .flatMap(existingClient -> validateClientUpdate(existingClient, request))
                .flatMap(clientRepository::save)
                .map(ClientResponse::fromEntity);
    }

    private Mono<Client> validateClientUpdate(Client existingClient, ClientRequest request) {
        return Mono.just(existingClient)
                .map(client -> {
                    client.setFirstName(request.getFirstName());
                    client.setLastName(request.getLastName());
                    client.setType(request.getType());
                    client.setPhone(request.getPhone());
                    // No permitimos cambiar el email si ya existe para otro cliente
                    if (!existingClient.getEmail().equals(request.getEmail())) {
                        return checkNewEmail(client, request.getEmail());
                    }
                    return Mono.just(client);
                })
                .flatMap(clientMono -> clientMono);
    }

    private Mono<Client> checkNewEmail(Client client, String newEmail) {
        return clientRepository.findByEmail(newEmail)
                .flatMap(existing -> Mono.error(new ApiValidateException("Email already in use")))
                .defaultIfEmpty(client)
                .map(c -> {
                    client.setEmail(newEmail);
                    return client;
                });
    }

    @Override
    public Flux<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .map(ClientResponse::fromEntity);
    }}


