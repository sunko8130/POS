package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class ReceiveItemViewResponse {

	@SerializedName("data")
	private ReceiveItemView data;

	@SerializedName("status")
	private Status status;

	public ReceiveItemView getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}