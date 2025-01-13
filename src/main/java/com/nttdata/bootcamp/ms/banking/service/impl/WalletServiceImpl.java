package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.entity.Wallet;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.mapper.WalletMapper;
import com.nttdata.bootcamp.ms.banking.repository.CreditCardRepository;
import com.nttdata.bootcamp.ms.banking.repository.WalletRepository;
import com.nttdata.bootcamp.ms.banking.service.CreditCardService;
import com.nttdata.bootcamp.ms.banking.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


/**
 * Implementación del servicio de tarjetas de crédito.
 * Proporciona operaciones para crear, consultar,
 * actualizar el saldo y bloquear tarjetas de crédito.
 *
 * <p>Esta clase utiliza un repositorio de tarjetas de
 * crédito y un mapper para convertir entre objetos de
 * dominio y DTOs. Además, verifica si un cliente tiene
 * deudas pendientes antes de permitir la creación
 * de una nueva tarjeta.</p>
 *
 * @author Bruno Andre Castro Barrientos
 * @version 1.1
 */

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

  private final WalletRepository walletRepository;
  //private final CreditCardMapper creditCardMapper;
  //private final WebClient webClient;

  @Value("${service.credit.url}")
  private String creditsServiceUrl;


  @Override
  public Mono<WalletResponse> createWallet(WalletRequest request) {
    Wallet wallet = WalletMapper.toEntity(request);
    wallet.setBalance(0.0);
    return walletRepository.save(wallet).map(WalletMapper::toResponse);
  }

  @Override
  public Mono<WalletResponse> getWalletByPhoneNumber(String phoneNumber) {
    return walletRepository.findByPhoneNumber(phoneNumber)
        .switchIfEmpty(Mono.error(new RuntimeException("Wallet not found")))
        .map(WalletMapper::toResponse);
  }

  @Override
  public Mono<WalletResponse> associateDebitCard(String phoneNumber, String debitCardNumber) {
    return walletRepository.findByPhoneNumber(phoneNumber)
        .switchIfEmpty(Mono.error(new RuntimeException("Wallet not found")))
        .flatMap(wallet -> {
          // Simulate debit card association logic
          // Assuming debitCardNumber is valid and belongs to the user
          return walletRepository.save(wallet);
        })
        .map(WalletMapper::toResponse);
  }

  @Override
  public Mono<WalletResponse> sendPayment(String fromPhoneNumber, String toPhoneNumber, Double amount) {
    return walletRepository.findByPhoneNumber(fromPhoneNumber)
        .switchIfEmpty(Mono.error(new RuntimeException("Sender wallet not found")))
        .flatMap(fromWallet -> walletRepository.findByPhoneNumber(toPhoneNumber)
            .switchIfEmpty(Mono.error(new RuntimeException("Recipient wallet not found")))
            .flatMap(toWallet -> {
              if (fromWallet.getBalance() < amount) {
                return Mono.error(new RuntimeException("Insufficient balance"));
              }
              fromWallet.setBalance(fromWallet.getBalance() - amount);
              toWallet.setBalance(toWallet.getBalance() + amount);

              return walletRepository.save(fromWallet)
                  .then(walletRepository.save(toWallet))
                  .then(Mono.just(WalletMapper.toResponse(fromWallet)));
            })
        );
  }
}