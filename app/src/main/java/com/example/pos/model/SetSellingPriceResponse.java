package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class SetSellingPriceResponse{

	@SerializedName("status")
	private Status status;

	public Status getStatus(){
		return status;
	}
}