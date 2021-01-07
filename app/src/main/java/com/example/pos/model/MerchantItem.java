package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class MerchantItem {
	@SerializedName("id")
	private int id;

	@SerializedName("itemId")
	private int itemId;

	@SerializedName("itemName")
	private String itemName;

	@SerializedName("totalQty")
	private int totalQty;

	@SerializedName("merchantId")
	private int merchantId;

	@SerializedName("itemCode")
	private String itemCode;

	public int getItemId(){
		return itemId;
	}

	public String getItemName(){
		return itemName;
	}

	public int getTotalQty(){
		return totalQty;
	}

	public int getMerchantId(){
		return merchantId;
	}

	public String getItemCode(){
		return itemCode;
	}

	public int getId() {
		return id;
	}
}