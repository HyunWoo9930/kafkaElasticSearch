package com.example.demo.consumer;

// Java Program to Illustrate Kafka Consumer

// Importing required classes
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

  @KafkaListener(topics = "your-topic-name")
  public void receiveMessage(String message) {
    log.info("Received message: {}", message);
  }
}



