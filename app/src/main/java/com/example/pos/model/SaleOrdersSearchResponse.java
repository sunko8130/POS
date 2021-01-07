package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SaleOrdersSearchResponse{

	@SerializedName("data")
	private List<SaleOrder> data;

	@SerializedName("status")
	private Status status;

	public List<SaleOrder> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}