package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class Bal{

	@SerializedName("cateCount")
	private int cateCount;

	@SerializedName("subCateCount")
	private int subCateCount;

	@SerializedName("itemCount")
	private int itemCount;

	public int getCateCount(){
		return cateCount;
	}

	public int getSubCateCount(){
		return subCateCount;
	}

	public int getItemCount(){
		return itemCount;
	}
}