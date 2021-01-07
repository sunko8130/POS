package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class SaleOrderDetailResponse{

	@SerializedName("data")
	private SaleOrderDetail data;

	@SerializedName("status")
	private Status status;

	public SaleOrderDetail getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}