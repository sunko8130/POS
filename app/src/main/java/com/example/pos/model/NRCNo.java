package com.example.pos.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NRCNo {

	@SerializedName("code")
	private int code;

	@SerializedName("nrcs")
	private List<NRCNation> nrcNations;

	public int getCode(){
		return code;
	}

	public List<NRCNation> getNrcNations(){
		return nrcNations;
	}
}