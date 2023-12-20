package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;
import com.kogi.emr.models.Tag;
import org.springframework.data.domain.Page;

import java.util.List;

public class TagsResponse {
  private String message;
  private List<Tag> tags;

  public TagsResponse(String message, List<Tag> tags) {
    this.message = message;
    this.tags = tags;
  }

  public List<Tag> getTags() {
    return tags;
  }

  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


}
