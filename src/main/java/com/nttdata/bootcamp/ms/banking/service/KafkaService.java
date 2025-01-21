package com.nttdata.bootcamp.ms.banking.service;

import com.nttdata.bootcamp.ms.banking.dto.request.CreditCardRequest;
import com.nttdata.bootcamp.ms.banking.dto.response.CreditCardResponse;
import reactor.core.publisher.Mono;

public interface KafkaService {
  void sendMessage(String message);
  void listen(String message);
}

