package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class SearchItem {

	@SerializedName("itemId")
	private int itemId;

	@SerializedName("itemName")
	private String itemName;

	@SerializedName("merchantId")
	private int merchantId;

	@SerializedName("uomName")
	private String uomName;

	@SerializedName("subCategoryId")
	private int subCategoryId;

	@SerializedName("categoryName")
	private String categoryName;

	@SerializedName("totalBaseUomQty")
	private int totalBaseUomQty;

	@SerializedName("categoryId")
	private int categoryId;

	@SerializedName("subCategoryName")
	private String subCategoryName;

	public int getItemId(){
		return itemId;
	}

	public String getItemName(){
		return itemName;
	}

	public int getMerchantId(){
		return merchantId;
	}

	public String getUomName(){
		return uomName;
	}

	public int getSubCategoryId(){
		return subCategoryId;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public int getTotalBaseUomQty(){
		return totalBaseUomQty;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public String getSubCategoryName(){
		return subCategoryName;
	}
}