package com.nttdata.bootcamp.ms.banking.model.response;

import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.model.CreditStatus;
import com.nttdata.bootcamp.ms.banking.model.CreditType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Clase Response que representa la respuesta de un crédito.
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class CreditResponse {

    private String id;
    private String clientId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private CreditStatus status;
    private CreditType creditType;
    private BigDecimal remainingBalance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convierte la entidad Credit a un CreditResponse.
     *
     * @param credit La entidad Credit.
     * @return El CreditResponse.
     */
    public static CreditResponse fromEntity(Credit credit) {
        CreditResponse response = new CreditResponse();
        response.setId(credit.getId());
        response.setClientId(credit.getClientId());
        response.setAmount(credit.getAmount());
        response.setInterestRate(credit.getInterestRate());
        response.setStatus(credit.getStatus());
        response.setCreditType(credit.getCreditType());
        response.setRemainingBalance(credit.getRemainingBalance());
        response.setCreatedAt(credit.getCreatedAt());
        response.setUpdatedAt(credit.getUpdatedAt());
        return response;
    }
}
