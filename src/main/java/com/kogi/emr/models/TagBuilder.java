package com.kogi.emr.models;

/**
 * This class is used to build a tag object.
 */
public class TagBuilder {
    private String name;

    public TagBuilder() {
    }

    public TagBuilder withName(String name) {
        this.name = name;
        return this;
    }


    public Tag build() {
        return new Tag(name);
    }
}

