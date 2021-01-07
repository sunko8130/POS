package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("item")
    private String item;

    @SerializedName("uom")
    private String uom;

    @SerializedName("total")
    private double total;

    @SerializedName("price")
    private double price;

    @SerializedName("comPer")
    private double comPer;

    @SerializedName("taxPer")
    private double taxPer;

    @SerializedName("qty")
    private int qty;

    public double getTaxPer() {
        return taxPer;
    }

    public String getItem() {
        return item;
    }

    public String getUom() {
        return uom;
    }

    public double getTotal() {
        return total;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public double getComPer() {
        return comPer;
    }
}