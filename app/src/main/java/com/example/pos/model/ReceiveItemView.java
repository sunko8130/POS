package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ReceiveItemView {

	@SerializedName("deliveryOrderNo")
	private String deliveryOrderNo;

	@SerializedName("receiveOrderNo")
	private String receiveOrderNo;

	@SerializedName("totalAmount")
	private double totalAmount;

	@SerializedName("taxPer")
	private double taxPer;

	@SerializedName("commPer")
	private double commPer;

	@SerializedName("taxAmount")
	private double taxAmount;

	@SerializedName("commAmount")
	private double commAmount;

	@SerializedName("resList")
	private List<OrderItem> resList;

	@SerializedName("receivePersonName")
	private String receivePersonName;

	public String getDeliveryOrderNo(){
		return deliveryOrderNo;
	}

	public String getReceiveOrderNo(){
		return receiveOrderNo;
	}

	public double getTotalAmount(){
		return totalAmount;
	}

	public double getTaxPer(){
		return taxPer;
	}

	public double getCommPer(){
		return commPer;
	}

	public double getTaxAmount(){
		return taxAmount;
	}

	public double getCommAmount(){
		return commAmount;
	}

	public List<OrderItem> getResList(){
		return resList;
	}

	public String getReceivePersonName(){
		return receivePersonName;
	}

}