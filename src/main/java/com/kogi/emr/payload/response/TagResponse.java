package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;
import com.kogi.emr.models.Tag;

public class TagResponse {
  private String message;
  private Tag tag;


  public TagResponse(String message, Tag tag) {
    this.message = message;
    this.tag = tag;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
