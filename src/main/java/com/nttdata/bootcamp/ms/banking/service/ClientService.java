package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.entity.Client;
import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.response.ClientResponse;
import reactor.core.publisher.Mono;

public interface ClientService {

    Mono<ClientResponse> createClient(ClientRequest request);

    Mono<ClientResponse> getClientByEmail(String email);

    Mono<Void> deleteClientById(String clientId);

    Mono<ClientResponse> updateClient(String clientId, ClientRequest request);
}

