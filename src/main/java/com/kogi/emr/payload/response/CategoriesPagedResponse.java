package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;
import org.springframework.data.domain.Page;

public class CategoriesPagedResponse {
  private String message;
  private Page<Category> categories;


  public CategoriesPagedResponse(String message, Page<Category> categories) {
    this.message = message;
    this.categories = categories;
  }

  public Page<Category> getCategories() {
    return categories;
  }

  public void setCategories(Page<Category> categories) {
    this.categories = categories;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


}
