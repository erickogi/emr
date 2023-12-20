package com.kogi.emr.payload.response;

import com.kogi.emr.models.Item;
import com.kogi.emr.models.Order;

public class OrderResponse {
  private String message;
  private Order order;

  public OrderResponse(String message, Order order) {
    this.message = message;
    this.order = order;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
