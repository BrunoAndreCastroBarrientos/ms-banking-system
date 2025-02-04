package com.nttdata.bootcamp.ms.banking.bootcoin.service.impl;

import com.nttdata.bootcamp.ms.banking.bootcoin.dto.request.WalletRequest;
import com.nttdata.bootcamp.ms.banking.bootcoin.dto.response.WalletResponse;
import com.nttdata.bootcamp.ms.banking.bootcoin.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.bootcoin.mapper.WalletMapper;
import com.nttdata.bootcamp.ms.banking.bootcoin.repository.WalletRepository;
import com.nttdata.bootcamp.ms.banking.bootcoin.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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