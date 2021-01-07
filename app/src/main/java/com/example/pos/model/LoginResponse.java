package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse{

	@SerializedName("data")
	private LoginData data;

	@SerializedName("status")
	private Status status;

	public LoginData getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}