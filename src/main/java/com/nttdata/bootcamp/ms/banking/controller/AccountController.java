package com.nttdata.bootcamp.ms.banking.controller;

import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.model.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.model.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Controller for managing account-related operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Endpoints for managing accounts.")
public class AccountController {

    private final AccountService accountService;

    /**
     * Creates a new account.
     *
     * @param request the account details to create.
     * @return a Mono containing the created account response.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new account", description = "Creates a new account with the provided details.")
    public Mono<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        return accountService.createAccount(request);
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number to search for.
     * @return a Mono containing the account response.
     */
    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get account by account number", description = "Retrieves account details using the account number.")
    public Mono<AccountResponse> getAccountByNumber(
            @PathVariable
            @Parameter(description = "The account number to search for.", required = true)
            String accountNumber) {
        return accountService.getAccountByNumber(accountNumber);
    }

    /**
     * Updates an existing account.
     *
     * @param accountId the ID of the account to update.
     * @param request the updated account details.
     * @return a Mono containing the updated account response.
     */
    @PutMapping("/{accountId}")
    @Operation(summary = "Update an account", description = "Updates the details of an existing account.")
    public Mono<AccountResponse> updateAccount(
            @PathVariable
            @Parameter(description = "The ID of the account to update.", required = true)
            String accountId,
            @Valid @RequestBody AccountRequest request) {
        return accountService.updateAccount(accountId, request);
    }

    /**
     * Deletes an account by its ID.
     *
     * @param accountId the ID of the account to delete.
     */
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an account", description = "Deletes an account using its ID.")
    public Mono<Void> deleteAccountById(
            @PathVariable
            @Parameter(description = "The ID of the account to delete.", required = true)
            String accountId) {
        return accountService.deleteAccountById(accountId);
    }
}


