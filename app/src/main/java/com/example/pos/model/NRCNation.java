package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class NRCNation {

	@SerializedName("nrc")
	private String nrc;

	public String getNrc(){
		return nrc;
	}
}