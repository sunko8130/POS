package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaleOrder implements Serializable {

	@SerializedName("salesDate")
	private String salesDate;

	@SerializedName("salesAmount")
	private double salesAmount;

	@SerializedName("salesNo")
	private String salesNo;

	public String getSalesDate(){
		return salesDate;
	}

	public double getSalesAmount(){
		return salesAmount;
	}

	public String getSalesNo(){
		return salesNo;
	}
}