package com.atyourdoorteam.atyourdoor.models.FinalOrderedItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopId {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("shopOwnerID")
    @Expose
    private String shopOwnerID;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("shopImageURL")
    @Expose
    private String shopImageURL;
    @SerializedName("shopAddress")
    @Expose
    private String shopAddress;
    @SerializedName("shopNumber")
    @Expose
    private String shopNumber;
    @SerializedName("shopLocation")
    @Expose
    private List<Double> shopLocation = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopOwnerID() {
        return shopOwnerID;
    }

    public void setShopOwnerID(String shopOwnerID) {
        this.shopOwnerID = shopOwnerID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImageURL() {
        return shopImageURL;
    }

    public void setShopImageURL(String shopImageURL) {
        this.shopImageURL = shopImageURL;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public List<Double> getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(List<Double> shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
