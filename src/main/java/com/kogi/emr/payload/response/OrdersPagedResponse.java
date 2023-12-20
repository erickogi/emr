package com.kogi.emr.payload.response;

import com.kogi.emr.models.Order;
import org.springframework.data.domain.Page;

public class OrdersPagedResponse {
  private String message;
  private Page<Order> orders;


  public OrdersPagedResponse(String message, Page<Order> orders) {
    this.message = message;
    this.orders = orders;
  }



  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Page<Order> getOrders() {
    return orders;
  }

  public void setOrders(Page<Order> orders) {
    this.orders = orders;
  }
}
