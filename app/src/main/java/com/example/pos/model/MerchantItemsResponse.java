package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MerchantItemsResponse{

	@SerializedName("data")
	private List<MerchantItem> data;

	@SerializedName("status")
	private Status status;

	public List<MerchantItem> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}