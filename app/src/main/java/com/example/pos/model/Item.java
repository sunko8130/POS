package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class Item {

	@SerializedName("code")
	private String code;

	@SerializedName("imagePath")
	private Object imagePath;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("subCategoryId")
	private int subCategoryId;

	@SerializedName("categoryId")
	private double categoryId;

	@SerializedName("status")
	private String status;

	public String getCode(){
		return code;
	}

	public Object getImagePath(){
		return imagePath;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public int getSubCategoryId(){
		return subCategoryId;
	}

	public double getCategoryId(){
		return categoryId;
	}

	public String getStatus(){
		return status;
	}
}