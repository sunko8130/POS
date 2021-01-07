package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class State {

	@SerializedName("townships")
	private List<Township> townships;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setTownships(List<Township> townships){
		this.townships = townships;
	}

	public List<Township> getTownships(){
		return townships;
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
			"State{" +
			"townships = '" + townships + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}