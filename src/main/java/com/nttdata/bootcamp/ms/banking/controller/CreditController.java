package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.model.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.model.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/credit")
@Tag(name = "Credit", description = "Endpoints for managing credits.")
public class CreditController {

    private final CreditService creditService;

    /**
     * Crea un nuevo crédito para un cliente, validando las restricciones según el tipo de cliente (personal o empresarial).
     *
     * @param creditRequest Datos del crédito a crear.
     * @return Mono con la respuesta que contiene los detalles del crédito creado.
     *
     * @see CreditRequest
     */
    @Operation(summary = "Crear un nuevo crédito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Crédito creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditResponse> createCredit(@RequestBody CreditRequest creditRequest) {
        return creditService.createCredit(creditRequest);
    }

    /**
     * Actualiza los detalles de un crédito existente, respetando las políticas de actualización de créditos.
     *
     * @param creditId El ID único del crédito a actualizar.
     * @param creditRequest Nuevos detalles del crédito.
     * @return Mono con la respuesta que contiene los detalles del crédito actualizado.
     */
    @Operation(summary = "Actualizar los detalles de un crédito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Crédito no encontrado")
    })
    @PutMapping("/{creditId}")
    public Mono<CreditResponse> updateCredit(
            @PathVariable String creditId,
            @RequestBody CreditRequest creditRequest
    ) {
        return creditService.updateCredit(creditId, creditRequest);
    }

    /**
     * Elimina un crédito del sistema, asegurando que el crédito exista antes de la eliminación.
     *
     * @param creditId El ID del crédito a eliminar.
     * @return Mono vacío indicando que la eliminación se completó correctamente.
     */
    @Operation(summary = "Eliminar un crédito")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Crédito eliminado con éxito"),
            @ApiResponse(responseCode = "404", description = "Crédito no encontrado")
    })
    @DeleteMapping("/{creditId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCredit(@PathVariable String creditId) {
        return creditService.deleteCredit(creditId);
    }

    /**
     * Obtiene los detalles de un crédito por su ID, verificando que el crédito exista.
     *
     * @param creditId El ID único del crédito a buscar.
     * @return Mono con la respuesta que contiene los detalles del crédito encontrado.
     *
     * @see CreditResponse
     */
    @Operation(summary = "Obtener los detalles de un crédito por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crédito encontrado"),
            @ApiResponse(responseCode = "404", description = "Crédito no encontrado")
    })
    @GetMapping("/{creditId}")
    public Mono<CreditResponse> getCreditById(@PathVariable String creditId) {
        return creditService.getCreditById(creditId);
    }

    /**
     * Obtiene todos los créditos asociados a un cliente, considerando los productos activos del cliente.
     *
     * @param clientId El ID único del cliente cuyos créditos se desean obtener.
     * @return Flux con los detalles de los créditos asociados al cliente.
     *
     * @see CreditResponse
     */
    @Operation(summary = "Obtener los créditos asociados a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Créditos encontrados"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/client/{clientId}")
    public Flux<CreditResponse> getCreditsByClientId(@PathVariable String clientId) {
        return creditService.getCreditsByClientId(clientId);
    }

}
