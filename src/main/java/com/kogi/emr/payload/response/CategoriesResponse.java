package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public class CategoriesResponse {
  private String message;
  private List<Category> categories;


  public CategoriesResponse(String message, List<Category> categories) {
    this.message = message;
    this.categories = categories;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


}
