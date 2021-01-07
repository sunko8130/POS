package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse{

	@SerializedName("status")
	private Status status;

	public Status getStatus(){
		return status;
	}
}