package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class StockBalanceResponse {

	@SerializedName("data")
	private List<StockBalance> data;

	@SerializedName("status")
	private Status status;

	public List<StockBalance> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}