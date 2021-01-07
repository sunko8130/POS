package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class StockBalance {

	@SerializedName("itemId")
	private int itemId;

	@SerializedName("itemName")
	private String itemName;

	@SerializedName("remainQty")
	private int remainQty;

	@SerializedName("itemCode")
	private String itemCode;

	@SerializedName("receiveQty")
	private int receiveQty;

	@SerializedName("saleQty")
	private int saleQty;

	@SerializedName("uomId")
	private int uomId;

	@SerializedName("uomName")
	private String uomName;

	@SerializedName("balText")
	private String balText;

	@SerializedName("receiveText")
	private String receiveText;

	public int getItemId(){
		return itemId;
	}

	public String getItemName(){
		return itemName;
	}

	public int getRemainQty(){
		return remainQty;
	}

	public String getItemCode(){
		return itemCode;
	}

	public int getReceiveQty(){
		return receiveQty;
	}

	public int getSaleQty(){
		return saleQty;
	}

	public int getUomId() {
		return uomId;
	}

	public String getUomName() {
		return uomName;
	}

	public String getBalText() {
		return balText;
	}

	public String getReceiveText() {
		return receiveText;
	}
}