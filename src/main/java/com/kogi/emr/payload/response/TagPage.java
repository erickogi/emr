package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;
import com.kogi.emr.models.Tag;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class TagPage extends PageImpl<Tag> {
    public TagPage(List<Tag> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}