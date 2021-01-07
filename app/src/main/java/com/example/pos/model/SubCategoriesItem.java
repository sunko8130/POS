package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class SubCategoriesItem{

	@SerializedName("code")
	private Object code;

	@SerializedName("createdDate")
	private Object createdDate;

	@SerializedName("updatedBy")
	private int updatedBy;

	@SerializedName("createdBy")
	private int createdBy;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	@SerializedName("updatedDate")
	private Object updatedDate;

	@SerializedName("categoryId")
	private int categoryId;

	@SerializedName("status")
	private Object status;

	public Object getCode(){
		return code;
	}

	public Object getCreatedDate(){
		return createdDate;
	}

	public int getUpdatedBy(){
		return updatedBy;
	}

	public int getCreatedBy(){
		return createdBy;
	}

	public String getName(){
		return name;
	}

	public int getId(){
		return id;
	}

	public Object getUpdatedDate(){
		return updatedDate;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public Object getStatus(){
		return status;
	}
}