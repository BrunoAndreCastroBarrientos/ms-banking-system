package com.nttdata.bootcamp.ms.banking.model.request;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * Request object for creating or updating an Account.
 * Contains validation rules and a method to map to the Account entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotBlank(message = "Account number is required.")
    @Size(max = 20, message = "Account number must not exceed 20 characters.")
    private String accountNumber;

    @NotNull(message = "Account type is required.")
    private AccountType type;

    @NotBlank(message = "Client ID is required.")
    @Pattern(regexp = "^[a-fA-F0-9]{24}$", message = "Client ID must be a valid 24-character hexadecimal.")
    private String clientId;

    @NotNull(message = "Balance is required.")
    @DecimalMin(value = "0.0", inclusive = true, message = "Balance must be zero or positive.")
    private Double balance;

    @NotNull(message = "Active transaction status is required.")
    private Boolean hasActiveTransactions;

    /**
     * Converts this request object to an Account entity.
     *
     * @return an Account entity with the data from this request.
     */
    public Account toEntity() {
        return Account.builder()
                .accountNumber(this.accountNumber)
                .type(this.type)
                .clientId(this.clientId)
                .balance(this.balance)
                .hasActiveTransactions(this.hasActiveTransactions)
                .build();
    }
}
