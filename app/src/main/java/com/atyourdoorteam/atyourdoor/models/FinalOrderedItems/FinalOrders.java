package com.atyourdoorteam.atyourdoor.models.FinalOrderedItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FinalOrders {
    @SerializedName("isPaid")
    @Expose
    private Boolean isPaid;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("shopId")
    @Expose
    private ShopId shopId;
    @SerializedName("orderMode")
    @Expose
    private String orderMode;
    @SerializedName("orderedItems")
    @Expose
    private List<FinalOrderedList> orderedItems = null;
    @SerializedName("shippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public FinalOrders(Boolean isPaid, String id, String user, ShopId shopId, String orderMode, List<FinalOrderedList> orderedItems, String shippingAddress, Integer totalPrice, String createdAt) {
        this.isPaid = isPaid;
        this.id = id;
        this.user = user;
        this.shopId = shopId;
        this.orderMode = orderMode;
        this.orderedItems = orderedItems;
        this.shippingAddress = shippingAddress;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ShopId getShopId() {
        return shopId;
    }

    public void setShopId(ShopId shopId) {
        this.shopId = shopId;
    }

    public String getOrderMode() {
        return orderMode;
    }

    public void setOrderMode(String orderMode) {
        this.orderMode = orderMode;
    }

    public List<FinalOrderedList> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<FinalOrderedList> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
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
