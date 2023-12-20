package com.kogi.emr.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PatchItemRequest {
	@NotBlank
	@Size(max = 50)
	private String name;


	private String description;

	@NotBlank
	private Integer reorderPoint;

	@NotBlank
	private Integer quantityAvailable;


	private Integer quantityOnHold;

	@NotBlank
	private Integer minimumStockLevel;


	private String metadata;

	private String categoryUUID;

	private String categoryName;

	private String categoryID;

	private List<Integer> tagIds;

	private Double costPrice;

	public PatchItemRequest(String name, String description, Integer reorderPoint, Integer quantityAvailable, Integer quantityOnHold, Integer minimumStockLevel,
							String metadata, String categoryUUID, String categoryName, String categoryID,Double costPrice) {
		this.name = name;
		this.description = description;
		this.reorderPoint = reorderPoint;
		this.quantityAvailable = quantityAvailable;
		this.quantityOnHold = quantityOnHold;
		this.minimumStockLevel = minimumStockLevel;
		this.metadata = metadata;
		this.categoryUUID = categoryUUID;
		this.categoryName = categoryName;
		this.categoryID = categoryID;
		this.costPrice = costPrice;
	}

	public PatchItemRequest(String name, String description, Integer reorderPoint, Integer quantityAvailable, Integer quantityOnHold, Integer minimumStockLevel,
							String metadata, String categoryUUID,Double costPrice) {
		this.name = name;
		this.description = description;
		this.reorderPoint = reorderPoint;
		this.quantityAvailable = quantityAvailable;
		this.quantityOnHold = quantityOnHold;
		this.minimumStockLevel = minimumStockLevel;
		this.metadata = metadata;
		this.categoryUUID = categoryUUID;
		this.costPrice = costPrice;
	}

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

	public String getCategoryUUID() {
		return categoryUUID;
	}

	public void setCategoryUUID(String categoryUUID) {
		this.categoryUUID = categoryUUID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public Double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	public List<Long> getTagIDs() {
		List<Long> ids = null;
		for (Integer id : tagIds) {
			ids.add(Long.valueOf(id));
		}
		return ids;
	}

	public void setTagIDs(List<Integer> tagIds) {
		this.tagIds = tagIds;
	}
}
