package com.nttdata.bootcamp.ms.banking.repository;

import com.nttdata.bootcamp.ms.banking.entity.Transaction;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
  /**
   * Encuentra todas las transacciones asociadas a una cuenta de origen específica.
   *
   * @param originAccountId ID de la cuenta de origen.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByOriginAccountId(String originAccountId);

  /**
   * Encuentra todas las transacciones asociadas a una cuenta de destino específica.
   *
   * @param destinationAccountId ID de la cuenta de destino.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByDestinationAccountId(String destinationAccountId);

  /**
   * Encuentra todas las transacciones asociadas a un crédito específico.
   *
   * @param creditId ID del crédito.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByCreditId(String creditId);

  /**
   * Encuentra todas las transacciones asociadas a una tarjeta de crédito específica.
   *
   * @param creditCardId ID de la tarjeta de crédito.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByCreditCardId(String creditCardId);

  /**
   * Encuentra todas las transacciones asociadas a una tarjeta de débito específica.
   *
   * @param debitCardId ID de la tarjeta de débito.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByDebitCardId(String debitCardId);

  /**
   * Encuentra todas las transacciones que ocurren dentro de un rango de fechas.
   *
   * @param startDate Fecha inicial.
   * @param endDate   Fecha final.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * Encuentra todas las transacciones asociadas a un tipo específico.
   *
   * @param transactionType Tipo de transacción.
   * @return Flujo de transacciones.
   */
  Flux<Transaction> findByTransactionType(String transactionType);
}


