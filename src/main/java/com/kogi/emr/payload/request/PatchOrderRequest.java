package com.kogi.emr.payload.request;

import jakarta.validation.constraints.NotBlank;

public class PatchOrderRequest {
	@NotBlank
	private Integer quantity;

	@NotBlank
	private Integer itemUUID;

	@OrderStatusFormat
	private String orderStatus;


	public PatchOrderRequest(Integer quantity, Integer itemUUID, String orderStatus) {
		this.quantity = quantity;
		this.itemUUID = itemUUID;
		this.orderStatus = orderStatus;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getItemUUID() {
		return itemUUID;
	}

	public void setItemUUID(Integer itemUUID) {
		this.itemUUID = itemUUID;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}
