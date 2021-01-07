package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class Township {

	@SerializedName("stateId")
	private int stateId;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Township{" +
			"stateId = '" + stateId + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}