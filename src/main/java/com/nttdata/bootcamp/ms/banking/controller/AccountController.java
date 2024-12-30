package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.model.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.model.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Controller for managing account-related operations.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Endpoints for managing accounts.")
public class AccountController {

    private final AccountService accountService;

    /**
     * Crea una nueva cuenta bancaria, asegurándose de que el tipo de cuenta y los límites del cliente sean cumplidos.
     * <p>
     * Los clientes personales solo pueden tener una cuenta de ahorro, corriente o plazo fijo, y los clientes empresariales
     * solo pueden tener cuentas corrientes. Las cuentas a plazo fijo permiten solo un movimiento por mes.
     * </p>
     *
     * @param accountRequest Datos de la cuenta a crear, incluyendo tipo de cuenta y cliente asociado.
     * @return Mono con la respuesta de la cuenta creada, incluyendo los detalles de la cuenta creada.
     */
    @Operation(summary = "Crear una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.CREATED_CODE, description = "Cuenta creada con éxito"),
            @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Datos inválidos proporcionados, posiblemente debido a reglas de negocio")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return accountService.createAccount(accountRequest);
    }

    /**
     * Actualiza una cuenta bancaria existente, asegurándose de que las reglas de negocio sean respetadas.
     * <p>
     * Los clientes personales solo pueden modificar cuentas dentro de los tipos permitidos para ellos (ahorro, corriente, plazo fijo),
     * mientras que los clientes empresariales solo pueden modificar cuentas corrientes.
     * </p>
     *
     * @param accountId El ID de la cuenta a actualizar.
     * @param accountRequest Datos de la cuenta actualizada, incluyendo tipo de cuenta y modificaciones del saldo.
     * @return Mono con la respuesta de la cuenta actualizada, incluyendo los nuevos detalles de la cuenta.
     */
    @Operation(summary = "Actualizar una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Cuenta actualizada con éxito"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cuenta no encontrada o no se cumplen las reglas de negocio para actualización")
    })
    @PutMapping("/{accountId}")
    public Mono<AccountResponse> updateAccount(
            @PathVariable String accountId,
            @Valid @RequestBody AccountRequest accountRequest
    ) {
        return accountService.updateAccount(accountId, accountRequest);
    }

    /**
     * Elimina una cuenta bancaria.
     * <p>
     * La eliminación de la cuenta debe seguir las reglas de negocio que evitan eliminar cuentas vinculadas a créditos activos
     * o que pertenezcan a un cliente con productos pendientes de pago.
     * </p>
     *
     * @param accountId El ID de la cuenta a eliminar.
     * @return Mono vacío indicando que la cuenta fue eliminada con éxito.
     */
    @Operation(summary = "Eliminar una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.DELETED_CODE, description = "Cuenta eliminada con éxito"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cuenta no encontrada o vinculada a productos activos")
    })
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAccount(@PathVariable String accountId) {
        return accountService.deleteAccount(accountId);
    }

    /**
     * Obtiene los detalles de una cuenta bancaria por su ID.
     * <p>
     * Este método devuelve la información detallada de la cuenta solicitada, asegurando que solo el propietario o un firmante autorizado
     * pueda consultar los datos sensibles.
     * </p>
     *
     * @param accountId El ID de la cuenta.
     * @return Mono con la respuesta de la cuenta encontrada, incluyendo todos los detalles de la cuenta.
     */
    @Operation(summary = "Obtener los detalles de una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Cuenta encontrada"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cuenta no encontrada o acceso no autorizado")
    })
    @GetMapping("/{accountId}")
    public Mono<AccountResponse> getAccountById(@PathVariable String accountId) {
        return accountService.getAccountById(accountId);
    }

    /**
     * Obtiene todas las cuentas asociadas a un cliente.
     * <p>
     * Este método devuelve todas las cuentas bancarias asociadas a un cliente, garantizando que se respeten las limitaciones
     * según el tipo de cliente (personal o empresarial).
     * </p>
     *
     * @param clientId El ID del cliente.
     * @return Flux con las cuentas asociadas al cliente, considerando las restricciones de tipo de cuenta por cliente.
     */
    @Operation(summary = "Obtener todas las cuentas asociadas a un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Cuentas encontradas"),
            @ApiResponse(responseCode = ConstantUtil.NOT_FOUND_CODE, description = "Cliente no encontrado o no tiene cuentas asociadas")
    })
    @GetMapping("/client/{clientId}")
    public Flux<AccountResponse> getAccountsByClientId(@PathVariable String clientId) {
        return accountService.getAccountsByClientId(clientId);
    }

    /**
     * Realiza un depósito en una cuenta bancaria.
     * <p>
     * Este método realiza un depósito, asegurando que el saldo de la cuenta no exceda los límites establecidos para el tipo de cuenta,
     * por ejemplo, en cuentas a plazo fijo, el depósito debe cumplir con las condiciones contractuales específicas.
     * </p>
     *
     * @param accountId El ID de la cuenta donde se realizará el depósito.
     * @param amount El monto a depositar.
     * @return Mono con la respuesta de la cuenta después del depósito, mostrando el saldo actualizado.
     */
    @Operation(summary = "Realizar un depósito en una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Depósito realizado con éxito"),
            @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Monto de depósito inválido o violación de reglas de negocio (como límite de saldo o condiciones de cuenta a plazo fijo)")
    })
    @PostMapping("/{accountId}/deposit")
    public Mono<AccountResponse> deposit(
            @PathVariable String accountId,
            @RequestParam BigDecimal amount
    ) {
        return accountService.deposit(accountId, amount);
    }

    /**
     * Realiza un retiro de una cuenta bancaria.
     * <p>
     * Este método permite un retiro siempre que el saldo sea suficiente, cumpliendo las reglas de negocio que impiden retirar
     * fondos de cuentas a plazo fijo si no es el día permitido para el movimiento.
     * </p>
     *
     * @param accountId El ID de la cuenta de la que se realizará el retiro.
     * @param amount El monto a retirar.
     * @return Mono con la respuesta de la cuenta después del retiro, mostrando el saldo actualizado.
     */
    @Operation(summary = "Realizar un retiro de una cuenta bancaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Retiro realizado con éxito"),
            @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Fondos insuficientes o no se cumple con las restricciones de la cuenta")
    })
    @PostMapping("/{accountId}/withdraw")
    public Mono<AccountResponse> withdraw(
            @PathVariable String accountId,
            @RequestParam BigDecimal amount
    ) {
        return accountService.withdraw(accountId, amount);
    }

    /**
     * Realiza una transferencia entre dos cuentas bancarias.
     * <p>
     * Este método permite transferir fondos entre dos cuentas dentro del mismo banco, siempre que se cumplan las reglas
     * de negocio, como tener fondos suficientes y verificar las cuentas de origen y destino.
     * </p>
     *
     * @param fromAccountId El ID de la cuenta desde la cual se realizará la transferencia.
     * @param toAccountId El ID de la cuenta a la cual se transferirán los fondos.
     * @param amount El monto a transferir.
     * @return Mono con la respuesta de la cuenta de origen y la cuenta destino después de la transferencia, mostrando
     *         el saldo actualizado.
     */
    @Operation(summary = "Realizar una transferencia entre cuentas bancarias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = ConstantUtil.OK_CODE, description = "Transferencia realizada con éxito"),
            @ApiResponse(responseCode = ConstantUtil.ERROR_CODE, description = "Fondos insuficientes o problemas con las cuentas")
    })
    @PostMapping("/transfer")
    public Mono<Void> transfer(
            @RequestParam String fromAccountId,
            @RequestParam String toAccountId,
            @RequestParam BigDecimal amount
    ) {
        return accountService.transfer(fromAccountId, toAccountId, amount);
    }

}


