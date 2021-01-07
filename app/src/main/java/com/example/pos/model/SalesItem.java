package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class SalesItem {
    @SerializedName("itemCode")
    private String itemCode;
    @SerializedName("itemName")
    private String itemName;
    @SerializedName("uom")
    private String uom;
    @SerializedName("uomId")
    private int uomId;
    @SerializedName("price")
    private double price;
    @SerializedName("total")
    private double amount;
    @SerializedName("qty")
    private String quantity;

    public SalesItem() {
    }

    public SalesItem(String itemCode, String itemName, String uom, int uomId, double price, double amount, String quantity) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.uom = uom;
        this.uomId = uomId;
        this.price = price;
        this.amount = amount;
        this.quantity = quantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public int getUomId() {
        return uomId;
    }

    public void setUomId(int uomId) {
        this.uomId = uomId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
