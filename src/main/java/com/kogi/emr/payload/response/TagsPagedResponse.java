package com.kogi.emr.payload.response;

import com.kogi.emr.models.Tag;
import org.springframework.data.domain.Page;

public class TagsPagedResponse {
  private String message;
  private Page<Tag> tags;

  public TagsPagedResponse(String message, Page<Tag> tags) {
    this.message = message;
    this.tags = tags;
  }

  public Page<Tag> getTags() {
    return tags;
  }

  public void setTags(Page<Tag> tags) {
    this.tags = tags;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }


}
