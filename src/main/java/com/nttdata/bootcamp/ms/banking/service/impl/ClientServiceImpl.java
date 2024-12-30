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
    public Mono<ClientResponse> createClient(ClientRequest clientRequest) {
        Client client = clientRequest.toEntity();
        return clientRepository.save(client)
                .map(ClientResponse::fromEntity);  // Convertir la entidad a ClientResponse
    }

    @Override
    public Mono<ClientResponse> updateClient(String clientId, ClientRequest clientRequest) {
        return clientRepository.findById(clientId)
                .flatMap(existingClient -> {
                    existingClient.setFirstName(clientRequest.getFirstName());
                    existingClient.setLastName(clientRequest.getLastName());
                    existingClient.setEmail(clientRequest.getEmail());
                    existingClient.setPhone(clientRequest.getPhone());
                    existingClient.setUpdatedAt(clientRequest.getUpdatedAt());
                    return clientRepository.save(existingClient)
                            .map(ClientResponse::fromEntity);  // Convertir la entidad a ClientResponse
                })
                .switchIfEmpty(Mono.error(new ApiValidateException("Cliente no encontrado")));
    }

    @Override
    public Mono<Void> deleteClient(String clientId) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new ApiValidateException("Cliente no encontrado"))) // Validamos si el cliente existe
                .flatMap(client -> clientRepository.delete(client));
    }

    @Override
    public Mono<ClientResponse> getClientById(String clientId) {
        return clientRepository.findById(clientId)
                .map(ClientResponse::fromEntity)  // Convertir la entidad a ClientResponse
                .switchIfEmpty(Mono.error(new ApiValidateException("Cliente no encontrado")));
    }

    @Override
    public Flux<ClientResponse> getAllClients() {
        return clientRepository.findAll()
                .map(ClientResponse::fromEntity);  // Convertir la entidad a ClientResponse
    }
}
