package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class DeleteSaleItemResponse{

	@SerializedName("status")
	private Status status;

	@SerializedName("balance")
	private String balance;

	public Status getStatus(){
		return status;
	}

	public String getBalance() {
		return balance;
	}
}