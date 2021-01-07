package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class GenerateReceiveNoResponse {
	@SerializedName("data")
	private String data;

	@SerializedName("status")
	private Status status;

	public String getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}
