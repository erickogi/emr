package com.kogi.emr.models;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * This class is used to build a item object.
 */
public class ItemBuilder {


    private String name;


    private String description;


    private Integer reorderPoint;


    private Integer quantityAvailable;


    private Integer quantityOnHold;


    private Integer minimumStockLevel;


    private String metadata;



    private Category category;


    private Set<Tag> tags;

    private Double costPrice;

    public ItemBuilder() {
    }


    public ItemBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ItemBuilder withReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
        return this;
    }


    public ItemBuilder withQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
        return this;
    }

    public ItemBuilder withQuantityOnHold(Integer quantityOnHold ) {
        this.quantityOnHold = quantityOnHold;
        return this;
    }

    public ItemBuilder withMinimumStockLevel(Integer minimumStockLevel ) {
        this.minimumStockLevel = minimumStockLevel;
        return this;
    }

    public ItemBuilder withMetadata(String metadata ) {
        this.metadata = metadata;
        return this;
    }

    public ItemBuilder withCategory(Category category ) {
        this.category = category;
        return this;
    }

    public ItemBuilder withTag(Set<Tag> tags ) {
        this.tags = tags;
        return this;
    }

    public ItemBuilder withCostPrice(Double costPrice ) {
        this.costPrice = costPrice;
        return this;
    }

    public Item build() {
        return new Item(name, description, reorderPoint, quantityAvailable, quantityOnHold, minimumStockLevel, metadata, category, tags,costPrice);
    }

}

