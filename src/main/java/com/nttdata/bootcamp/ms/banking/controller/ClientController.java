package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.model.request.ClientRequest;
import com.nttdata.bootcamp.ms.banking.model.response.ClientResponse;
import com.nttdata.bootcamp.ms.banking.service.ClientService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for managing client-related operations.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Endpoints for managing clients.")
public class ClientController {

    private final ClientService clientService;

    /**
     * Crea un nuevo cliente en el sistema, validando el tipo de cliente (personal o empresarial) y sus productos.
     *
     * @param clientRequest Datos del cliente a crear.
     * @return Mono con la respuesta que contiene los detalles del cliente creado.
     *
     * @see ClientRequest
     */
    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = "Cliente creado con éxito"),
            @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Datos inválidos proporcionados")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClientResponse> createClient(@Valid @RequestBody ClientRequest clientRequest) {
        return clientService.createClient(clientRequest);
    }

    /**
     * Actualiza los detalles de un cliente, considerando restricciones de productos activos y tipo de cliente.
     *
     * @param clientId El ID único del cliente a actualizar.
     * @param clientRequest Nuevos datos del cliente.
     * @return Mono con la respuesta que contiene los detalles del cliente actualizado.
     */
    @Operation(summary = "Actualizar los detalles de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = "Cliente actualizado con éxito"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cliente no encontrado")
    })
    @PutMapping("/{clientId}")
    public Mono<ClientResponse> updateClient(
            @PathVariable String clientId,
            @Valid @RequestBody ClientRequest clientRequest
    ) {
        return clientService.updateClient(clientId, clientRequest);
    }

    /**
     * Elimina un cliente, asegurándose de que no tenga productos activos como cuentas o créditos.
     *
     * @param clientId El ID del cliente a eliminar.
     * @return Mono vacío indicando que la eliminación se completó correctamente.
     */
    @Operation(summary = "Eliminar un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = "Cliente eliminado con éxito"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cliente no encontrado")
    })
    @DeleteMapping("/{clientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteClient(@PathVariable String clientId) {
        return clientService.deleteClient(clientId);
    }

    /**
     * Obtiene los detalles de un cliente por su ID, considerando que el cliente debe estar registrado y sin productos activos impeditivos.
     *
     * @param clientId El ID del cliente a buscar.
     * @return Mono con la respuesta que contiene los detalles del cliente.
     *
     * @see ClientResponse
     */
    @Operation(summary = "Obtener los detalles de un cliente por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Cliente encontrado"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cliente no encontrado")
    })
    @GetMapping("/{clientId}")
    public Mono<ClientResponse> getClientById(@PathVariable String clientId) {
        return clientService.getClientById(clientId);
    }

    /**
     * Obtiene todos los clientes registrados en el sistema.
     *
     * @return Flux con los detalles de todos los clientes.
     */
    @Operation(summary = "Obtener todos los clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Clientes encontrados"),
            @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Error interno del servidor")
    })
    @GetMapping
    public Flux<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }

}
