package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ItemsPriceResponse{

	@SerializedName("data")
	private List<ItemPrice> data;

	@SerializedName("status")
	private Status status;

	public List<ItemPrice> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}