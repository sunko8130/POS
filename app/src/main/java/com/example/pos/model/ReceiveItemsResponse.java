package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ReceiveItemsResponse{

	@SerializedName("data")
	private List<ReceiveItem> data;

	@SerializedName("status")
	private Status status;

	public List<ReceiveItem> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}