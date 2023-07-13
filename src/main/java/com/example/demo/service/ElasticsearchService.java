package com.example.demo.service;

import com.example.demo.dataModel.MessageData;
import com.example.demo.dataModel.Order;
import com.example.demo.dataModel.Order.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ElasticsearchService {

  @Autowired
  private RestHighLevelClient elasticsearchClient;

  public void indexOrder(Order order) {
    IndexRequest indexRequest = new IndexRequest("data-optimize-test-index")
        .id(order.getId())
        .source(convertOrderToMap(order), XContentType.JSON);

    try {
      IndexResponse indexResponse = elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT);
      log.debug("indexResponse = {}", indexResponse);
      // Handle index response if necessary
    } catch (IOException e) {
      // Handle index request failure
      e.printStackTrace();
    }
  }

  // Other methods for search, update, delete, etc.

  private Map<String, Object> convertOrderToMap(Order order) {
    Map<String, Object> orderMap = new HashMap<>();

    // Add individual fields to the map
    orderMap.put("id", order.getId());
    orderMap.put("timestamp", order.getTimestamp());

    // Convert user details to a nested map
    Map<String, Object> userMap = new HashMap<>();
    userMap.put("id", order.getUser().getId());
    userMap.put("name", order.getUser().getName());
    userMap.put("email", order.getUser().getEmail());
    orderMap.put("user", userMap);

    // Convert products to a list of nested maps
    List<Map<String, Object>> productsList = new ArrayList<>();
    for (Product product : order.getProducts()) {
      Map<String, Object> productMap = new HashMap<>();
      productMap.put("id", product.getId());
      productMap.put("name", product.getName());
      productMap.put("price", product.getPrice());
      productMap.put("category", product.getCategory());
      productsList.add(productMap);
    }
    orderMap.put("products", productsList);

    // Convert shipping address to a nested map
    Map<String, Object> addressMap = new HashMap<>();
    addressMap.put("street", order.getShippingAddress().getStreet());
    addressMap.put("city", order.getShippingAddress().getCity());
    addressMap.put("state", order.getShippingAddress().getState());
    addressMap.put("zip_code", order.getShippingAddress().getZipCode());
    orderMap.put("shipping_address", addressMap);

    return orderMap;
  }

}


