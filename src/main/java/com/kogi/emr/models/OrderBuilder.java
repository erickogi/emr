package com.kogi.emr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

/**
 * This class is used to build a order object.
 */
public class OrderBuilder {

    private Integer quantity;


    private Item item;


    private User creator;

    private OrderStatus orderStatus;

    public OrderBuilder() {
    }

    public OrderBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderBuilder withItem(Item item) {
        this.item = item;
        return this;
    }



    public OrderBuilder withOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public OrderBuilder withCreator(User creator) {
        this.creator = creator;
        return this;
    }

    public Order build() {
        return new Order(quantity, item, creator, orderStatus);
    }
}

