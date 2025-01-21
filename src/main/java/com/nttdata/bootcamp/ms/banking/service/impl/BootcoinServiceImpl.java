package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.dto.enumeration.RecordStatus;
import com.nttdata.bootcamp.ms.banking.dto.enumeration.TransactionBootCoinType;
import com.nttdata.bootcamp.ms.banking.dto.request.BootCoinRequest;
import com.nttdata.bootcamp.ms.banking.dto.request.CustomerRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.BootCoinResponse;
import com.nttdata.bootcamp.ms.banking.dto.response.CustomerResponse;
import com.nttdata.bootcamp.ms.banking.entity.BootCoinTransaction;
import com.nttdata.bootcamp.ms.banking.entity.Customer;
import com.nttdata.bootcamp.ms.banking.exception.ApiValidateException;
import com.nttdata.bootcamp.ms.banking.mapper.BootCoinMapper;
import com.nttdata.bootcamp.ms.banking.mapper.CustomerMapper;
import com.nttdata.bootcamp.ms.banking.repository.BootCoinRepository;
import com.nttdata.bootcamp.ms.banking.repository.CustomerRepository;
import com.nttdata.bootcamp.ms.banking.repository.WalletRepository;
import com.nttdata.bootcamp.ms.banking.service.BootcoinService;
import com.nttdata.bootcamp.ms.banking.service.CustomerService;
import com.nttdata.bootcamp.ms.banking.utility.ConstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Implementación del servicio de clientes. Proporciona operaciones
 * para crear, actualizar, consultar y cambiar el estado de los clientes.
 *
 * <p>Este servicio gestiona la creación, actualización y consulta de
 * los clientes, además de validar reglas de negocio como la verificación
 * de perfiles VIP o PYME y la comprobación de deudas pendientes antes de
 * la creación de un cliente.</p>
 *
 * @version 1.1
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class BootcoinServiceImpl implements BootcoinService {

  private final BootCoinRepository bootCoinRepository;
  private final WalletRepository walletRepository;

  public Mono<BootCoinResponse> buyBootCoin(BootCoinRequest request, BigDecimal amount) {
    return walletRepository.findByPhoneNumber(request.getPhoneNumber())
        .flatMap(wallet -> {
          BigDecimal walletBalance = wallet.getBalance();

          if (walletBalance.compareTo(amount) < 0) {
            return Mono.error(new ApiValidateException("Insufficient funds"));
          }

          wallet.setBalance(walletBalance.subtract(amount));
          return walletRepository.save(wallet)
              .then(bootCoinRepository.save(
                  BootCoinTransaction.builder()
                      .phoneNumber(request.getPhoneNumber())
                      .amount(request.getAmount())
                      .transactionBootCoinType(TransactionBootCoinType.BUY)
                      .transactionDate(LocalDateTime.now())
                      .build()              ));
        })
        .map(BootCoinMapper::toResponse);
  }

  public Mono<BootCoinResponse> sellBootCoin(BootCoinRequest request, BigDecimal amount) {
    return bootCoinRepository.findUserBootCoinBalance(request.getPhoneNumber())
        .flatMap(balance -> {
          if (balance.compareTo(amount) < 0) {
            return Mono.error(new ApiValidateException("Insufficient BootCoin balance"));
          }

          return walletRepository.findByPhoneNumber(request.getPhoneNumber())
              .flatMap(wallet -> {
                BigDecimal walletBalance = wallet.getBalance();
                wallet.setBalance(walletBalance.add(amount));
                return walletRepository.save(wallet)
                    .then(bootCoinRepository.save(
                        BootCoinTransaction.builder()
                            .phoneNumber(request.getPhoneNumber())
                            .amount(request.getAmount())
                            .transactionBootCoinType(TransactionBootCoinType.SELL)
                            .transactionDate(LocalDateTime.now())
                            .build()              ));
              });
        })
        .map(BootCoinMapper::toResponse);
  }
}
