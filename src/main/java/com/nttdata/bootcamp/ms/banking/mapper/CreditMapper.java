package com.nttdata.bootcamp.ms.banking.mapper;

import com.nttdata.bootcamp.ms.banking.entity.Credit;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditResponse;
import org.springframework.stereotype.Component;

@Component
public class CreditMapper {

  public static Credit toEntity(CreditRequest request) {
    Credit credit = new Credit();
    credit.setCustomerId(request.getCustomerId());
    credit.setCreditType(request.getCreditType());
    credit.setPrincipalAmount(request.getPrincipalAmount());
    credit.setOutstandingBalance(request.getPrincipalAmount()); // Inicialmente igual al principal
    credit.setInterestRate(request.getInterestRate());
    credit.setMonthlyPayment(request.getMonthlyPayment());
    credit.setDueDate(request.getDueDate());
    credit.setStatus(RecordStatus.ACTIVE);  // Por defecto, se crea como ACTIVO
    return credit;
  }

  public CreditResponse toResponse(Credit credit) {
    CreditResponse response = new CreditResponse();
    response.setId(credit.getId());
    response.setCustomerId(credit.getCustomerId());
    response.setCreditType(credit.getCreditType().name());
    response.setPrincipalAmount(credit.getPrincipalAmount());
    response.setOutstandingBalance(credit.getOutstandingBalance());
    response.setInterestRate(credit.getInterestRate());
    response.setMonthlyPayment(credit.getMonthlyPayment());
    response.setDueDate(credit.getDueDate());
    response.setStatus(credit.getStatus().name());
    // Definir criterio para overdue, por ejemplo, si la dueDate está pasada o su status es BLOCKED/MOROSO
    response.setOverdue(isCreditOverdue(credit));
    return response;
  }

  /**
   * Ejemplo simple de cuándo considerar un crédito como "overdue".
   * Podría basarse en status, fecha, etc.
   */
  private static boolean isCreditOverdue(Credit credit) {
    // Por ejemplo, si la dueDate ya pasó y el credit sigue sin pagar
    if (credit.getDueDate() != null && credit.getDueDate().isBefore(java.time.LocalDate.now())) {
      return true;
    }
    // Podríamos chequear si su status es BLOCKED o algo similar
    return false;
  }
}