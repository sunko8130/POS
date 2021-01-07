package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ItemSearchResponse{

	@SerializedName("data")
	private List<SearchItem> data;

	@SerializedName("bal")
	private Bal bal;

	@SerializedName("status")
	private Status status;

	public List<SearchItem> getData(){
		return data;
	}

	public Bal getBal(){
		return bal;
	}

	public Status getStatus(){
		return status;
	}
}