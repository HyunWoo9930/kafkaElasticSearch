package com.example.demo.service;

import com.example.demo.dataModel.MessageData;
import com.example.demo.dataModel.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  @Autowired
  private ElasticsearchService elasticsearchService;

  // Kafka message consumer
  @KafkaListener(topics = "dataOptimize-test-topic")
  public void consumeKafkaMessage(String message) {
    // Convert the Kafka message to the mapped data structure
    Order order = convertToOrder(message);

    // Pass the converted data to the Elasticsearch service for indexing
    elasticsearchService.indexOrder(order);
  }

  private Order convertToOrder(String message) {
    // Parse the JSON message and map it to the Order object
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.readValue(message, Order.class);
    } catch (JsonProcessingException e) {
      // Handle parsing errors
      e.printStackTrace();
      return null;
    }
  }
}


