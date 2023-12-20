package com.kogi.emr.payload.response;

import com.kogi.emr.models.Item;
import org.springframework.data.domain.Page;

public class ItemsResponse {
  private String message;
  private Page<Item> items;

  public ItemsResponse(String message, Page<Item> items) {
    this.message = message;
    this.items = items;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Page<Item> getItems() {
    return items;
  }

  public void setItems(Page<Item> items) {
    this.items = items;
  }
}
