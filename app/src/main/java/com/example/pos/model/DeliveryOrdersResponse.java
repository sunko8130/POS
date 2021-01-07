package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DeliveryOrdersResponse{

	@SerializedName("data")
	private List<OrderItem> data;

	@SerializedName("order")
	private OrderItem order;

	@SerializedName("status")
	private Status status;

	public List<OrderItem> getData(){
		return data;
	}

	public OrderItem getOrder(){
		return order;
	}

	public Status getStatus(){
		return status;
	}
}