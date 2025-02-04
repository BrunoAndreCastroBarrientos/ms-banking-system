package com.nttdata.bootcamp.ms.banking.transaction.service;

public interface KafkaService {
  void sendMessage(String message);

  //void listen(String message);
}

