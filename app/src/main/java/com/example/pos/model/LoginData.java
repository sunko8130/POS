package com.example.pos.model;

import com.google.gson.annotations.SerializedName;

public class LoginData {
    @SerializedName("id")
    private int id;

    @SerializedName("contactPhone2")
    private String contactPhone2;

    @SerializedName("address")
    private String address;

    @SerializedName("stateId")
    private int stateId;

    @SerializedName("postalCode")
    private int postalCode;

    @SerializedName("contactPhone1")
    private String contactPhone1;

    @SerializedName("userName")
    private String userName;

    @SerializedName("password")
    private String password;

    @SerializedName("ownerName")
    private String ownerName;

    @SerializedName("userLoginName")
    private String userLoginName;

    @SerializedName("storeName")
    private String storeName;

    @SerializedName("townshipId")
    private int townshipId;

    @SerializedName("nrc")
    private String nrc;

    @SerializedName("status")
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContactPhone2() {
        return contactPhone2;
    }

    public String getAddress() {
        return address;
    }

    public int getStateId() {
        return stateId;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getContactPhone1() {
        return contactPhone1;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getTownshipId() {
        return townshipId;
    }

    public String getNrc() {
        return nrc;
    }

    public String getStatus() {
        return status;
    }
}