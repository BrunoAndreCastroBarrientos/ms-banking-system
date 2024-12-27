package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.response.ClientResponse;
import com.nttdata.bootcamp.ms.banking.repository.ClientRepository;
import com.nttdata.bootcamp.ms.banking.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    public Mono<ClientResponse> createClient(ClientRequest request) {
        return clientRepository.save(request.toEntity())
                .flatMap(newClient -> clientRepository.save(newClient))
                .map(ClientResponse::fromEntity);
    }

    @Override
    public Mono<ClientResponse> getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .map(ClientResponse::fromEntity);
    }

    @Override
    public Mono<Void> deleteClientById(String clientId) {
        return clientRepository.deleteById(clientId);
    }
    public Mono<ClientResponse> updateClient(String clientId, ClientRequest request) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("Client not found with id: " + clientId)))
                .flatMap(existingClient -> {
                    existingClient.setFirstName(request.getFirstName());
                    existingClient.setLastName(request.getLastName());
                    existingClient.setType(request.getType());
                    existingClient.setEmail(request.getEmail());
                    existingClient.setPhone(request.getPhone());
                    return clientRepository.save(existingClient);
                })
                .map(ClientResponse::fromEntity);
    }

}


