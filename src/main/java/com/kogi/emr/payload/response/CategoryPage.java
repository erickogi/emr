package com.kogi.emr.payload.response;

import com.kogi.emr.models.Category;
import com.kogi.emr.models.Item;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CategoryPage extends PageImpl<Category> {
    public CategoryPage(List<Category> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}