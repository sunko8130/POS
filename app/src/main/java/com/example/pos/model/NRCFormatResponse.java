package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NRCFormatResponse{

	@SerializedName("data")
	private List<NRCNo> data;

	@SerializedName("status")
	private Status status;

	public List<NRCNo> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}
}