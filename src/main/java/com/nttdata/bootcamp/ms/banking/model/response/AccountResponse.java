package com.nttdata.bootcamp.ms.banking.model.response;

import com.nttdata.bootcamp.ms.banking.entity.Account;
import com.nttdata.bootcamp.ms.banking.model.AccountType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private String id;
    private String accountNumber;
    private AccountType type;
    private String clientId;
    private Double balance;
    private Boolean hasActiveTransactions;

    public static AccountResponse fromEntity(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .type(account.getType())
                .clientId(account.getClientId())
                .balance(account.getBalance())
                .hasActiveTransactions(account.getHasActiveTransactions())
                .build();
    }
}
