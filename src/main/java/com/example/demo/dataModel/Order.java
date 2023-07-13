package com.example.demo.dataModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "data-optimize-test-index")
@Getter
@Setter
public class Order {

  @Id
  private String id;
  private LocalDateTime timestamp;
  private User user;
  private List<Product> products;
  private ShippingAddress shippingAddress;

  @Getter
  @Setter
  public static class User {
    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
      this.id = id;
      this.name = name;
      this.email = email;
    }
  }

  @Getter
  @Setter
  public static class Product {
    private String id;
    private String name;
    private double price;
    private String category;

    public Product(String id, String name, double price, String category) {
      this.id = id;
      this.name = name;
      this.price = price;
      this.category = category;
    }
  }

  @Getter
  @Setter
  public static class ShippingAddress {
    private String street;
    private String city;
    private String state;
    private String zipCode;

    public ShippingAddress(String street, String city, String state, String zipCode) {
      this.street = street;
      this.city = city;
      this.state = state;
      this.zipCode = zipCode;
    }
  }

  public static Order create(String message) {
    JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
    Order order = new Order();
    JsonObject user = jsonObject.get("user").getAsJsonObject();
    User user1 = new User(user.get("id").getAsString(), user.get("name").getAsString(),
        user.get("email").getAsString());
    order.setUser(user1);
    JsonArray products = jsonObject.get("products").getAsJsonArray();
    List<Product> productList = new ArrayList<>();
    products.forEach(s -> {
      Product product = new Product(s.getAsJsonObject().get("id").getAsString(),
          s.getAsJsonObject().get("name").getAsString(),
          s.getAsJsonObject().get("price").getAsDouble(),
          s.getAsJsonObject().get("category").getAsString());
      productList.add(product);
    });
    order.setProducts(productList);
    JsonObject shippingAddress = jsonObject.get("shippingAddress").getAsJsonObject();
    ShippingAddress shippingAddress1 = new ShippingAddress(
        shippingAddress.get("street").getAsString(), shippingAddress.get("city").getAsString(),
        shippingAddress.get("state").getAsString(), shippingAddress.get("zipCode").getAsString());
    order.setShippingAddress(shippingAddress1);
    order.setTimestamp(LocalDateTime.now());
    return order;
  }

}

