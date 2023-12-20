package com.kogi.emr.payload.response;

import com.kogi.emr.models.Item;

public class ItemResponse {
  private String message;
  private Item item;

  public ItemResponse(String message, Item item) {
    this.message = message;
    this.item = item;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
