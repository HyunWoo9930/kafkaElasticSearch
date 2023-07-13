package com.example.demo.controller;

import com.example.demo.dataModel.Order;
import com.example.demo.dataModel.Order.Product;
import com.example.demo.dataModel.Order.ShippingAddress;
import com.example.demo.dataModel.Order.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dataOptimizeTest")
@Slf4j
public class DataOptimizeTestController {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public DataOptimizeTestController(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @GetMapping
  public void sendOrder() throws Exception {
    Order order = new Order();
    User user = new User("ohw9930", "오현우", "hw62459930@xdn.kr");
    Product product1 = new Product("p1", "Product 1", 29.99, "Electronics");
    Product product2 = new Product("p2", "Product 2", 49.99, "Home & Kitchen");
    List<Product> productList = new ArrayList<>();
    productList.add(product1);
    productList.add(product2);
    ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "Exampleville", "ABC", "12345");
    order.setUser(user);
    order.setProducts(productList);
    order.setShippingAddress(shippingAddress);

    // Serialize the Order object to JSON
    ObjectMapper objectMapper = new ObjectMapper();
    String orderJson = objectMapper.writeValueAsString(order);

    String topic = "dataOptimize-test-topic";
    kafkaTemplate.send(topic, orderJson);
    log.info("dataOptimize Message sent");
  }



}
