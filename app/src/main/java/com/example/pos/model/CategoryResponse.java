package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CategoryResponse{

	@SerializedName("data")
	private List<Category> data;

	@SerializedName("status")
	private Status status;

	public List<Category> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}