package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.response.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {
    Mono<ClientResponse> createClient(ClientRequest clientRequest);
    Mono<ClientResponse> updateClient(String clientId, ClientRequest clientRequest);
    Mono<Void> deleteClient(String clientId);
    Mono<ClientResponse> getClientById(String clientId);
    Flux<ClientResponse> getAllClients();
}

