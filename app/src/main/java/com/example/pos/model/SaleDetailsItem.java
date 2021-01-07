package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class SaleDetailsItem {

	@SerializedName("unitPrice")
	private double unitPrice;

	@SerializedName("itemName")
	private String itemName;

	@SerializedName("uom")
	private String uom;

	@SerializedName("createdDate")
	private long createdDate;

	@SerializedName("createdBy")
	private int createdBy;

	@SerializedName("itemCode")
	private String itemCode;

	@SerializedName("qty")
	private int qty;

	@SerializedName("salesNo")
	private String salesNo;

	@SerializedName("id")
	private int id;

	@SerializedName("uomId")
	private int uomId;

	@SerializedName("subTotal")
	private double subTotal;

	public double getUnitPrice(){
		return unitPrice;
	}

	public String getItemName(){
		return itemName;
	}

	public String getUom(){
		return uom;
	}

	public long getCreatedDate(){
		return createdDate;
	}

	public int getCreatedBy(){
		return createdBy;
	}

	public String getItemCode(){
		return itemCode;
	}

	public int getQty(){
		return qty;
	}

	public String getSalesNo(){
		return salesNo;
	}

	public int getId(){
		return id;
	}

	public double getSubTotal(){
		return subTotal;
	}

	public int getUomId() {
		return uomId;
	}
}