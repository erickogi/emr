package com.kogi.emr.payload.response;

import com.kogi.emr.models.Item;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ItemPage extends PageImpl<Item> {
    public ItemPage(List<Item> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}