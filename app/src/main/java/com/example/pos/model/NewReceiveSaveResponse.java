package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class NewReceiveSaveResponse{

	@SerializedName("status")
	private Status status;

	public Status getStatus(){
		return status;
	}
}