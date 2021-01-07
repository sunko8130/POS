package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiveItem implements Serializable {

	@SerializedName("receivedPersonName")
	private String receivedPersonName;

	@SerializedName("totalAmount")
	private double totalAmount;

	@SerializedName("createdDate")
	private String createdDate;

	@SerializedName("receiveOrderNo")
	private String receiveOrderNo;

	@SerializedName("id")
	private int id;

	public String getReceivedPersonName(){
		return receivedPersonName;
	}

	public double getTotalAmount(){
		return totalAmount;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public String getReceiveOrderNo(){
		return receiveOrderNo;
	}

	public int getId(){
		return id;
	}
}