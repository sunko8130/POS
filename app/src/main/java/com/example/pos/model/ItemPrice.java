package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class ItemPrice {

	@SerializedName("unitPrice")
	private double unitPrice;

	@SerializedName("itemName")
	private String itemName;

	@SerializedName("itemCode")
	private String itemCode;

	@SerializedName("uomName")
	private String uomName;

	@SerializedName("uomId")
	private int uomId;

	@SerializedName("id")
	private int id;

	public double getUnitPrice(){
		return unitPrice;
	}

	public String getItemName(){
		return itemName;
	}

	public String getItemCode(){
		return itemCode;
	}

	public String getUomName(){
		return uomName;
	}

	public int getUomId(){
		return uomId;
	}

	public int getId(){
		return id;
	}
}