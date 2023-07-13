package com.example.demo.dataModel;

import com.example.demo.dataModel.Order.Product;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "data-optimize-test-index")
@Getter
@Setter
public class OptimizedOrder {
  @Id
  private String orderId;
  private LocalDateTime timestamp;
  private String user_id;
  private String user_name;
  private String user_email;
  private List<Product> products;
  private String shippingAddress_street;
  private String shippingAddress_city;
  private String shippingAddress_state;
  private String shippingAddress_zipCode;

  @Builder(builderClassName = "of", builderMethodName = "of")
  public OptimizedOrder(String orderId, LocalDateTime timestamp, String user_id, String user_name,
      String user_email, List<Product> products, String shippingAddress_street,
      String shippingAddress_city, String shippingAddress_state, String shippingAddress_zipCode) {
    this.orderId = orderId;
    this.timestamp = timestamp;
    this.user_id = user_id;
    this.user_name = user_name;
    this.user_email = user_email;
    this.products = products;
    this.shippingAddress_street = shippingAddress_street;
    this.shippingAddress_city = shippingAddress_city;
    this.shippingAddress_state = shippingAddress_state;
    this.shippingAddress_zipCode = shippingAddress_zipCode;
  }

  public static OptimizedOrder toResponse(Order order) {
    return of()
        .orderId(order.getId())
        .timestamp(order.getTimestamp())
        .user_id(order.getUser().getId())
        .user_name(order.getUser().getName())
        .user_email(order.getUser().getEmail())
        .products(order.getProducts())
        .shippingAddress_street(order.getShippingAddress().getStreet())
        .shippingAddress_city(order.getShippingAddress().getCity())
        .shippingAddress_state(order.getShippingAddress().getState())
        .shippingAddress_zipCode(order.getShippingAddress().getZipCode())
        .build();
  }

}
