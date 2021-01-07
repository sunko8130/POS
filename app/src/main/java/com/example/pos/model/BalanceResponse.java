package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse{

	@SerializedName("data")
	private double data;

	@SerializedName("status")
	private Status status;

	public double getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}