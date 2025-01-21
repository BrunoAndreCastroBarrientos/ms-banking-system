package com.nttdata.bootcamp.ms.banking.service.impl;

import com.nttdata.bootcamp.ms.banking.service.KafkaService;
import lombok.RequiredArgsConstructor;
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
public class KafkaServiceImpl implements KafkaService {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${spring.kafka.topic.name:kafka-broker-1}")
  private String topicName;

  @Override
  public void sendMessage(String message) {
    try {
      kafkaTemplate.send(topicName, message);
      System.out.println("Message sent: " + message);
    } catch (Exception e) {
      System.err.println("Error sending message: " + e.getMessage());
    }
  }

  @KafkaListener(topics = "${spring.kafka.topic.name:kafka-broker-1}", groupId = "${spring.kafka.consumer.group-id}")
  public void listen(String message) {
    System.out.println("Message received: " + message);
  }
}

