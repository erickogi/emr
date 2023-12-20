package com.kogi.emr.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PatchCategoryRequest {
	@NotBlank
	@Size(max = 50)
	private String name;

	public PatchCategoryRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}