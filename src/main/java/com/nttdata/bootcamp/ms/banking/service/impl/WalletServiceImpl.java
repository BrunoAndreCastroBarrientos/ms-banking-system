package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionBootCoinType;
import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.entity.BootCoinTransaction;
import com.nttdata.bootcamp.ms.banking.entity.CreditCard;
import com.nttdata.bootcamp.ms.banking.entity.Wallet;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.BootCoinMapper;
import com.nttdata.bootcamp.ms.banking.mapper.CreditCardMapper;
import com.nttdata.bootcamp.ms.banking.mapper.WalletMapper;
import com.nttdata.bootcamp.ms.banking.repository.BootCoinRepository;
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
import java.time.LocalDateTime;


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
  private final WalletMapper walletMapper;

  public Mono<WalletResponse> sendPayment(WalletRequest request, BigDecimal amount) {
    return walletRepository.findByPhoneNumber(request.getPhoneNumber())
        .flatMap(senderWallet -> {
          BigDecimal senderBalance = senderWallet.getBalance();

          if (senderBalance.compareTo(amount) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds"));
          }

          senderWallet.setBalance(senderBalance.subtract(amount));
          return walletRepository.save(senderWallet);
        })
        .then(walletRepository.findByPhoneNumber(request.getPhoneNumber())
            .flatMap(recipientWallet -> {
              BigDecimal recipientBalance = recipientWallet.getBalance();
              recipientWallet.setBalance(recipientBalance.add(amount));
              return walletRepository.save(recipientWallet);
            })
        )
        .map(walletMapper::toResponse);
  }

  public Mono<WalletResponse> receivePayment(WalletRequest request, BigDecimal amount) {
    return walletRepository.findByPhoneNumber(request.getPhoneNumber())
        .flatMap(wallet -> {
          BigDecimal currentBalance = wallet.getBalance();
          wallet.setBalance(currentBalance.add(amount));
          return walletRepository.save(wallet);
        })
        .map(walletMapper::toResponse);
  }

  public Mono<WalletResponse> getWalletDetails(String phoneNumber) {
    return walletRepository.findByPhoneNumber(phoneNumber)
        .map(walletMapper::toResponse);
  }

}