package com.kogi.emr.payload.response;

import com.kogi.emr.models.Item;
import com.kogi.emr.models.Order;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class OrderPage extends PageImpl<Order> {
    public OrderPage(List<Order> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}