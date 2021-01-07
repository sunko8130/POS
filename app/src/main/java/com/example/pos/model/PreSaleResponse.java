package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class PreSaleResponse{

	@SerializedName("status")
	private Status status;

	public Status getStatus(){
		return status;
	}
}