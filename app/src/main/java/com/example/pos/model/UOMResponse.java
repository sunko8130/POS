package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UOMResponse{

	@SerializedName("data")
	private List<UOM> data;

	@SerializedName("status")
	private Status status;

	public List<UOM> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}