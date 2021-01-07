package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ItemsResponse{

	@SerializedName("data")
	private List<Item> data;

	@SerializedName("status")
	private Status status;

	public List<Item> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}