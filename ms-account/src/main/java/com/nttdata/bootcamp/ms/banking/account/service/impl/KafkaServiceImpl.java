package com.nttdata.bootcamp.ms.banking.account.service.impl;

import com.nttdata.bootcamp.ms.banking.account.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio de Kafka.
 *
 * @version 1.1
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${spring.kafka.topic.name:kafka-broker-1}")
  private String topicName;


  @KafkaListener(topics = "${spring.kafka.topic.name:kafka-broker-1}", groupId = "${spring.kafka.consumer.group-id}")
  public void listen(String message) {
    log.info("Message received: " + message);
  }
}

