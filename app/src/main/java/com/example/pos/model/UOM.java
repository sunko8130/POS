package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class UOM {

	@SerializedName("unitPrice")
	private double unitPrice;

	@SerializedName("uomName")
	private String uomName;

	@SerializedName("uomId")
	private int uomId;

	public double getUnitPrice(){
		return unitPrice;
	}

	public String getUomName(){
		return uomName;
	}

	public int getUomId(){
		return uomId;
	}
}