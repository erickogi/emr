package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;

public class CategoryResponse {
  private String message;
  private Category category;


    public CategoryResponse(String message, Category category) {
        this.message = message;
        this.category = category;
    }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
