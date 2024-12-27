package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.response.ClientResponse;
import com.nttdata.bootcamp.ms.banking.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for managing client-related operations.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Endpoints for managing clients.")
public class ClientController {

    private final ClientService clientService;

    /**
     * Creates a new client.
     *
     * @param request the client details to create.
     * @return a Mono containing the created client response.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new client", description = "Creates a new client with the provided details.")
    public Mono<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        return clientService.createClient(request);
    }

    /**
     * Retrieves a client by its email.
     *
     * @param email the email of the client to search for.
     * @return a Mono containing the client response.
     */
    @GetMapping("/{email}")
    @Operation(summary = "Get client by email", description = "Retrieves client details using the email.")
    public Mono<ClientResponse> getClientByEmail(
            @PathVariable
            @Parameter(description = "The email of the client to search for.", required = true)
            String email) {
        return clientService.getClientByEmail(email);
    }

    /**
     * Updates an existing client.
     *
     * @param clientId the ID of the client to update.
     * @param request the updated client details.
     * @return a Mono containing the updated client response.
     */
    @PutMapping("/{clientId}")
    @Operation(summary = "Update a client", description = "Updates the details of an existing client.")
    public Mono<ClientResponse> updateClient(
            @PathVariable
            @Parameter(description = "The ID of the client to update.", required = true)
            String clientId,
            @Valid @RequestBody ClientRequest request) {
        return clientService.updateClient(clientId, request);
    }

    /**
     * Deletes a client by its ID.
     *
     * @param clientId the ID of the client to delete.
     */
    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a client", description = "Deletes a client using its ID.")
    public Mono<Void> deleteClientById(
            @PathVariable
            @Parameter(description = "The ID of the client to delete.", required = true)
            String clientId) {
        return clientService.deleteClientById(clientId);
    }
}
