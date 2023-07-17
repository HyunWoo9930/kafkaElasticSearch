package com.example.demo.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledMessageSender {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public ScheduledMessageSender(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Scheduled(fixedDelay = 1000) // 10초마다 실행되도록 설정
  public void sendMessage() {
    String message = "Hello, Kafka!"; // 전송할 메시지
    String topic = "test-topic";
    kafkaTemplate.send(topic, message);
    log.info("Message sent: {}", message);
  }
}

