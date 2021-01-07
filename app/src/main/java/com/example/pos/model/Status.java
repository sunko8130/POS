package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class Status{

	@SerializedName("code")
	private int code;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private String status;

	public int getCode(){
		return code;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}