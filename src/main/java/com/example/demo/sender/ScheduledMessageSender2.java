package com.example.demo.sender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledMessageSender2 {

  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public ScheduledMessageSender2(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Scheduled(fixedDelay = 50000) // 50초마다 실행되도록 설정
  public void sendMessage() {
    String message = "Hello, Elasticsearch!"; // 전송할 메시지
    String topic = "test-topic2";
    kafkaTemplate.send(topic, message);
    log.info("Message sent: {}", message);
  }
}
