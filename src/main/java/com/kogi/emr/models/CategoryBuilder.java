package com.kogi.emr.models;

/**
 * This class is used to build a category object.
 */
public class CategoryBuilder {
    private String name;

    public CategoryBuilder() {
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }


    public Category build() {
        return new Category(name);
    }
}

