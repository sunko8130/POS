package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class Payment {

	@SerializedName("paymentNo")
	private String paymentNo;

	@SerializedName("totalAmount")
	private double totalAmount;

	@SerializedName("paidDate")
	private String paidDate;

	@SerializedName("remainAmount")
	private double remainAmount;

	@SerializedName("currentPaidAmount")
	private double currentPaidAmount;

	@SerializedName("deliveryNo")
	private String deliveryNo;

	public String getPaymentNo(){
		return paymentNo;
	}

	public double getTotalAmount(){
		return totalAmount;
	}

	public String getPaidDate(){
		return paidDate;
	}

	public double getRemainAmount(){
		return remainAmount;
	}

	public String getDeliveryNo(){
		return deliveryNo;
	}

	public double getCurrentPaidAmount() {
		return currentPaidAmount;
	}
}