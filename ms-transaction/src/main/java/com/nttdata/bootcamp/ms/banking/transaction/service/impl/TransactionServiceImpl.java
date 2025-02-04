package com.nttdata.bootcamp.ms.banking.transaction.service.impl;

import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.transaction.dto.enumeration.TransactionType;
import com.nttdata.bootcamp.ms.banking.transaction.dto.request.AccountRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.request.CreditRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.request.TransactionRequest;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.AccountResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.CreditResponse;
import com.nttdata.bootcamp.ms.banking.transaction.dto.response.TransactionResponse;
import com.nttdata.bootcamp.ms.banking.transaction.entity.Account;
import com.nttdata.bootcamp.ms.banking.transaction.entity.Credit;
import com.nttdata.bootcamp.ms.banking.transaction.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.transaction.entity.Transaction;
import com.nttdata.bootcamp.ms.banking.transaction.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.AccountMapper;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.CreditMapper;
import com.nttdata.bootcamp.ms.banking.transaction.mapper.TransactionMapper;
import com.nttdata.bootcamp.ms.banking.transaction.repository.TransactionRepository;
import com.nttdata.bootcamp.ms.banking.transaction.service.KafkaService;
import com.nttdata.bootcamp.ms.banking.transaction.service.TransactionService;
import com.nttdata.bootcamp.ms.banking.transaction.utility.ConstantUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

  private final TransactionRepository transactionRepository;
  private final WebClient accountWebClient;
  private final WebClient customerWebClient;
  private final WebClient creditWebClient;
  private final WebClient creditCardWebClient;
  private final AccountMapper accountMapper;
  private final CreditMapper creditMapper;
  private final CreditCardMapper creditCardMapper;
  private final TransactionMapper transactionMapper;
  private final KafkaService kafkaService;

  @Override
  @CircuitBreaker(name = "handleTransactionService", fallbackMethod = "handleTransactionFallback")
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
        return Mono.error(new ApiValidateException("Unsupported transaction type"));
    }
  }

  @CircuitBreaker(name = "handleTransactionService", fallbackMethod = "handleTransactionFallback")
  private Mono<TransactionResponse> handleDeposit(TransactionRequest request) {
    return this.findAccountById(request.getDestinationAccountId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .filter(account -> !account.getStatus().equals(RecordStatus.valueOf("INACTIVE")))
        .switchIfEmpty(Mono.error(new ApiValidateException("Account inactive.")))
        .flatMap(account -> {
          account.setBalance(account.getBalance().add(request.getAmount()));
          return this.saveAccount(accountMapper.entityToRequest(account));
        })
        .flatMap(savedAccount -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.DEPOSIT);
          return transactionRepository.save(transaction)
              .doOnSuccess(savedTransaction -> {
                kafkaService.sendMessage("TRANSACTION: " + savedTransaction.toString());
              });
        })
        .map(transactionMapper::toResponse);
  }

  @CircuitBreaker(name = "handleWithdrawalService", fallbackMethod = "handleWithdrawalFallback")
  public Mono<TransactionResponse> handleWithdrawal(TransactionRequest request) {
    return this.findAccountById(request.getOriginAccountId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .filter(account -> !account.getStatus().equals(RecordStatus.valueOf("INACTIVE")))
        .switchIfEmpty(Mono.error(new ApiValidateException("Account inactive.")))
        .flatMap(account -> {
          if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds"));
          }
          account.setBalance(account.getBalance().subtract(request.getAmount()));
          return this.saveAccount(accountMapper.entityToRequest(account));
        })
        .flatMap(account -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.WITHDRAWAL);
          return transactionRepository.save(transaction)
              .doOnSuccess(savedTransaction -> {
                kafkaService.sendMessage("TRANSACTION: " + savedTransaction.toString());
              });
        })
        .map(transactionMapper::toResponse);
  }

  private Mono<TransactionResponse> handleTransfer(TransactionRequest request) {
    return  this.findAccountById(request.getOriginAccountId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .filter(account -> !account.getStatus().equals(RecordStatus.valueOf("INACTIVE")))
        .switchIfEmpty(Mono.error(new ApiValidateException("Account inactive.")))
        .flatMap(originAccount -> {
          if (originAccount.getBalance().compareTo(request.getAmount()) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds"));
          }
          originAccount.setBalance(originAccount.getBalance().subtract(request.getAmount()));
          return this.saveAccount(accountMapper.entityToRequest(originAccount));
        })
        .then( this.findAccountById(request.getDestinationAccountId()))
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .filter(account -> !account.getStatus().equals(RecordStatus.valueOf("INACTIVE")))
        .flatMap(destinationAccount -> {
          destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
          return this.saveAccount(accountMapper.entityToRequest(destinationAccount));
        })
        .flatMap(destinationAccount -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.TRANSFER);
          return transactionRepository.save(transaction)
              .doOnSuccess(savedTransaction -> {
                kafkaService.sendMessage("TRANSACTION: " + savedTransaction.toString());
              });
        })
        .map(transactionMapper::toResponse);
  }

  @CircuitBreaker(name = "handleTransactionService", fallbackMethod = "handleTransactionFallback")
  private Mono<TransactionResponse> handleCreditPayment(TransactionRequest request) {
    return this.findCreditById(request.getCreditId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .filter(account -> !account.getStatus().equals(RecordStatus.valueOf("INACTIVE")))
        .switchIfEmpty(Mono.error(new ApiValidateException("Credit inactive.")))
        .flatMap(credit -> {
          credit.setDebt(credit.getDebt().subtract(request.getAmount()));
          return this.saveCredit(creditMapper.entityToRequest(credit));
        })
        .flatMap(credit -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.CREDIT_PAYMENT);
          return transactionRepository.save(transaction)
              .doOnSuccess(savedTransaction -> {
                kafkaService.sendMessage("TRANSACTION: " + savedTransaction.toString());
              });
        })
        .map(transactionMapper::toResponse);
  }

  @CircuitBreaker(name = "handleTransactionService", fallbackMethod = "handleTransactionFallback")
  private Mono<TransactionResponse> handleCreditCardPayment(TransactionRequest request) {
    return this.findCreditCardById(request.getCreditCardId())
        .switchIfEmpty(Mono.error(new ApiValidateException(ConstantUtil.NOT_FOUND_MESSAGE)))
        .filter(account -> !account.getStatus().equals(RecordStatus.valueOf("INACTIVE")))
        .switchIfEmpty(Mono.error(new ApiValidateException("CreditCard inactive.")))
        .flatMap(creditCard -> {
          creditCard.setBalance(creditCard.getBalance().subtract(request.getAmount()));
          return this.saveCreditCard(creditCardMapper.entityToRequest(creditCard));
        })
        .flatMap(creditCard -> {
          Transaction transaction = transactionMapper.toEntity(request);
          transaction.setTransactionType(TransactionType.CREDIT_CARD_PAYMENT);
          return transactionRepository.save(transaction)
              .doOnSuccess(savedTransaction -> {
                kafkaService.sendMessage("TRANSACTION: " + savedTransaction.toString());
              });
        })
        .map(transactionMapper::toResponse);
  }


  private Mono<Account> findAccountById(String id) {
    return accountWebClient.get()
        .uri("/{id}", id)
        .retrieve()
        .bodyToMono(AccountResponse.class)
        .map(accountMapper::responseToEntity)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")));
  }

  private Mono<Account> saveAccount(AccountRequest request) {
    return accountWebClient.post()
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AccountResponse.class)
        .map(accountMapper::responseToEntity)
        .onErrorMap(WebClientResponseException.class,
            e -> new ApiValidateException("Failed to save account: " + e.getMessage())); // Manejo de errores
  }

  private Mono<Credit> findCreditById(String id) {
    return creditWebClient.get()
        .uri("/{id}", id)
        .retrieve()
        .bodyToMono(CreditResponse.class)
        .map(creditMapper::responseToEntity)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")));
  }

  private Mono<Credit> saveCredit(CreditRequest request) {
    return creditWebClient.post()
        .bodyValue(request)
        .retrieve()
        .bodyToMono(CreditResponse.class)
        .map(creditMapper::responseToEntity)
        .onErrorMap(WebClientResponseException.class,
            e -> new ApiValidateException("Failed to save account: " + e.getMessage())); // Manejo de errores
  }

  private Mono<CreditCard> findCreditCardById(String id) {
    return creditCardWebClient.get()
        .uri("/{id}", id)
        .retrieve()
        .bodyToMono(CreditCardResponse.class)
        .map(creditCardMapper::responseToEntity)
        .switchIfEmpty(Mono.error(new ApiValidateException("Account not found")));
  }


  private Mono<CreditCard> saveCreditCard(CreditCardRequest request) {
    return creditCardWebClient.post()
        .bodyValue(request)
        .retrieve()
        .bodyToMono(CreditCardResponse.class)
        .map(creditCardMapper::responseToEntity)
        .onErrorMap(WebClientResponseException.class,
            e -> new ApiValidateException("Failed to save account: " + e.getMessage())); // Manejo de errores
  }
}