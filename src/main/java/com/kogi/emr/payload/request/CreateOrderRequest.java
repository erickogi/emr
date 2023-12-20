package com.kogi.emr.payload.request;

public class CreateOrderRequest {
	private Integer quantity;

	private Integer itemId;

	@OrderStatusFormat
	private String orderStatus;


	public CreateOrderRequest(Integer quantity, Integer itemId, String orderStatus) {
		this.quantity = quantity;
		this.itemId = itemId;
		this.orderStatus = orderStatus;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}


	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}
