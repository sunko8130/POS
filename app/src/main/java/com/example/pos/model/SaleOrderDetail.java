package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SaleOrderDetail {

	@SerializedName("total")
	private double total;

	@SerializedName("salesNo")
	private String salesNo;

	@SerializedName("details")
	private List<SaleDetailsItem> details;

	public double getTotal(){
		return total;
	}

	public String getSalesNo(){
		return salesNo;
	}

	public List<SaleDetailsItem> getDetails(){
		return details;
	}
}