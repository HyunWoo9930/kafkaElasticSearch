package com.example.demo.consumer;

// Java Program to Illustrate Kafka Consumer

import com.example.demo.dataModel.MessageData;
import com.example.demo.dataModel.MessageData2;
import com.example.demo.dataModel.OptimizedOrder;
import com.example.demo.dataModel.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@Component
@Slf4j
public class KafkaConsumer {

  private final ElasticsearchOperations elasticsearchOperations;

  @Autowired
  public KafkaConsumer(ElasticsearchOperations elasticsearchOperations) {
    this.elasticsearchOperations = elasticsearchOperations;
  }

  @KafkaListener(topics = "test-topic")
  public void receiveMessage(String message) {
    MessageData data = MessageData.create(message);
    elasticsearchOperations.save(data);
    log.info("Message indexed: {}", message);
  }

  @KafkaListener(topics = "test-topic2")
  public void receiveMessage2(String message) {
    MessageData2 data = MessageData2.create(message);
    elasticsearchOperations.save(data);
    log.info("Message indexed: {}", message);
  }

  @KafkaListener(topics = "dataOptimize-test-topic")
  public void receiveMessage3(String message) {
    Order order = Order.create(message);
    OptimizedOrder response = OptimizedOrder.toResponse(order);
    elasticsearchOperations.save(response);
    log.info("Message indexed: {}", message);
    log.info("Optimized Message indexed: {}", response);
  }
}






