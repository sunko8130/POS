package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateCityResponse {

	@SerializedName("data")
	private List<State> data;

	@SerializedName("status")
	private Status status;

	public void setData(List<State> data){
		this.data = data;
	}

	public List<State> getData(){
		return data;
	}

	public void setStatus(Status status){
		this.status = status;
	}

	public Status getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"StateCityResponse{" +
			"data = '" + data + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}