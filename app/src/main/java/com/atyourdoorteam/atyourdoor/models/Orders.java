package com.atyourdoorteam.atyourdoor.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {
    @SerializedName("shopId")
    @Expose
    private String shopId;
    @SerializedName("orderedItems")
    @Expose
    private List<OrderedItem> orderedItems = null;
    @SerializedName("shippingAddress")
    @Expose
    private String shippingAddress;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItem> orderedItems) {
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
}
