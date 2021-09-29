package com.atyourdoorteam.atyourdoor.models.FinalOrderedItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinalOrderedList {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("mainCategoryId")
    @Expose
    private String mainCategoryId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productImageURL")
    @Expose
    private String productImageURL;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productPrice")
    @Expose
    private String productPrice;
    @SerializedName("productQuantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("shopId")
    @Expose
    private String shopId;
    @SerializedName("subCategoryId")
    @Expose
    private String subCategoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
