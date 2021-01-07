package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PaymentResponse{

	@SerializedName("data")
	private List<Payment> data;

	@SerializedName("status")
	private Status status;

	public List<Payment> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}