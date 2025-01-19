package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionType;
import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.TransactionMapper;
import com.nttdata.bootcamp.ms.banking.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.repository.AccountRepository;
import com.nttdata.bootcamp.ms.banking.repository.CreditCardRepository;
import com.nttdata.bootcamp.ms.banking.repository.CreditRepository;
import com.nttdata.bootcamp.ms.banking.repository.TransactionRepository;
import com.nttdata.bootcamp.ms.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Implementación del servicio de transacciones.
 * Proporciona operaciones para procesar transacciones de depósitos, retiros,
 * transferencias, pagos de créditos y pagos de tarjetas de crédito.
 *
 * <p>Este servicio gestiona la lógica de negocio asociada a transacciones financieras
 * entre cuentas, créditos y tarjetas de crédito. También verifica el estado de las cuentas,
 * los créditos y las tarjetas, y aplica las comisiones necesarias en base
 * a las transacciones realizadas.</p>
 *
 * @version 1.1
 * @author Bruno Andre Castro Barrientos
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final CreditRepository creditRepository;
  private final CreditCardRepository creditCardRepository;
  private final TransactionMapper transactionMapper;

  public Mono<TransactionResponse> processTransaction(TransactionRequest request) {
    switch (request.getTransactionType()) {
      case DEPOSIT:
        return handleDeposit(request);
      case WITHDRAWAL:
        return handleWithdrawal(request);
      case TRANSFER:
        return handleTransfer(request);
      case CREDIT_PAYMENT:
        return handleCreditPayment(request);
      case CREDIT_CARD_PAYMENT:
        return handleCreditCardPayment(request);
      default:
        return Mono.error(new IllegalArgumentException("Unsupported transaction type"));
    }
  }

  private Mono<TransactionResponse> handleDeposit(TransactionRequest request) {
    return accountRepository.findById(request.getDestinationAccountId())
        .flatMap(account -> {
          account.setBalance(account.getBalance().add(request.getAmount()));
          return accountRepository.save(account);
        })
        .flatMap(account -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.DEPOSIT);
          return transactionRepository.save(transaction);
        })
        .map(transactionMapper::toResponse);
  }

  private Mono<TransactionResponse> handleWithdrawal(TransactionRequest request) {
    return accountRepository.findById(request.getOriginAccountId())
        .flatMap(account -> {
          if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds"));
          }
          account.setBalance(account.getBalance().subtract(request.getAmount()));
          return accountRepository.save(account);
        })
        .flatMap(account -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.WITHDRAWAL);
          return transactionRepository.save(transaction);
        })
        .map(transactionMapper::toResponse);
  }

  private Mono<TransactionResponse> handleTransfer(TransactionRequest request) {
    return accountRepository.findById(request.getOriginAccountId())
        .flatMap(originAccount -> {
          if (originAccount.getBalance().compareTo(request.getAmount()) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds"));
          }
          originAccount.setBalance(originAccount.getBalance().subtract(request.getAmount()));
          return accountRepository.save(originAccount);
        })
        .then(accountRepository.findById(request.getDestinationAccountId()))
        .flatMap(destinationAccount -> {
          destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
          return accountRepository.save(destinationAccount);
        })
        .flatMap(destinationAccount -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.TRANSFER);
          return transactionRepository.save(transaction);
        })
        .map(transactionMapper::toResponse);
  }

  private Mono<TransactionResponse> handleCreditPayment(TransactionRequest request) {
    return creditRepository.findById(request.getCreditId())
        .flatMap(credit -> {
          credit.setAmount(credit.getAmount().subtract(request.getAmount()));
          return creditRepository.save(credit);
        })
        .flatMap(credit -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.CREDIT_PAYMENT);
          return transactionRepository.save(transaction);
        })
        .map(transactionMapper::toResponse);
  }

  private Mono<TransactionResponse> handleCreditCardPayment(TransactionRequest request) {
    return creditCardRepository.findById(request.getCreditCardId())
        .flatMap(creditCard -> {
          creditCard.setBalance(creditCard.getBalance().subtract(request.getAmount()));
          return creditCardRepository.save(creditCard);
        })
        .flatMap(creditCard -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.CREDIT_CARD_PAYMENT);
          return transactionRepository.save(transaction);
        })
        .map(transactionMapper::toResponse);
  }
}