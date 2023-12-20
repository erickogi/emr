package com.kogi.emr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        })
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long code;

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name="uuid", insertable = false, updatable = false, nullable = false)
//    private UUID uuid;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private String description;


    private Integer reorderPoint;


    private Integer quantityAvailable;


    private Integer quantityOnHold;


    private Integer minimumStockLevel;

    @NotBlank
    private String metadata;




    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = true, updatable = false)
    private LocalDateTime updatedAt;


    @Column(name = "deleted_at", nullable = true, updatable = false)
    private LocalDateTime deletedAt;

    @ManyToOne
    private Category category;

    @Min(10)
    @Max(1000000)
    private Double costPrice;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(  name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();


    public Item() {
    }

    public Item(Long id, Long code, UUID uuid, String name, String description, Integer reorderPoint,
                Integer quantityAvailable, Integer quantityOnHold, Integer minimumStockLevel, String metadata,
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, Category category, Set<Tag> tags, Double costPrice) {
        this.id = id;
        //this.code = code;
        //this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.reorderPoint = reorderPoint;
        this.quantityAvailable = quantityAvailable;
        this.quantityOnHold = quantityOnHold;
        this.minimumStockLevel = minimumStockLevel;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.category = category;
        this.tags = tags;
        this.costPrice = costPrice;
    }

    public Item(String name, String description, Integer reorderPoint, Integer quantityAvailable, Integer quantityOnHold, Integer minimumStockLevel,
                String metadata, Category category, Set<Tag> tags, Double costPrice) {
        this.name = name;
        this.description = description;
        this.reorderPoint = reorderPoint;
        this.quantityAvailable = quantityAvailable;
        this.quantityOnHold = quantityOnHold;
        this.minimumStockLevel = minimumStockLevel;
        this.metadata = metadata;
        this.category = category;
        this.tags = tags;
        this.costPrice = costPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getCode() {
//        return code;
//    }
//
//    public void setCode(Long code) {
//        this.code = code;
//    }

//    public UUID getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(UUID uuid) {
//        this.uuid = uuid;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReorderPoint() {
        return reorderPoint;
    }

    public void setReorderPoint(Integer reorderPoint) {
        this.reorderPoint = reorderPoint;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Integer getQuantityOnHold() {
        return quantityOnHold;
    }

    public void setQuantityOnHold(Integer quantityOnHold) {
        this.quantityOnHold = quantityOnHold;
    }

    public Integer getMinimumStockLevel() {
        return minimumStockLevel;
    }

    public void setMinimumStockLevel(Integer minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Double costPrice) {
        this.costPrice = costPrice;
    }
}
