package com.kogi.emr.payload.response;

import com.kogi.emr.models.Item;
import com.kogi.emr.models.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public class OrdersResponse {
  private String message;
  private List<Order> orders;

  public OrdersResponse(String message, List<Order> orders) {
    this.message = message;
    this.orders = orders;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
