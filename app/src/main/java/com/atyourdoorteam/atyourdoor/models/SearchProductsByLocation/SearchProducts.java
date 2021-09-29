package com.atyourdoorteam.atyourdoor.models.SearchProductsByLocation;

import com.atyourdoorteam.atyourdoor.models.FinalOrderedItems.ShopId;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchProducts {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("shopId")
    @Expose
    private ShopId shopId;
    @SerializedName("mainCategoryId")
    @Expose
    private String mainCategoryId;
    @SerializedName("subCategoryId")
    @Expose
    private String subCategoryId;
    @SerializedName("productImageURL")
    @Expose
    private String productImageURL;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productPrice")
    @Expose
    private String productPrice;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public SearchProducts(String id, ShopId shopId, String mainCategoryId, String subCategoryId, String productImageURL, String productName, String productPrice) {
        this.id = id;
        this.shopId = shopId;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryId = subCategoryId;
        this.productImageURL = productImageURL;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShopId getShopId() {
        return shopId;
    }

    public void setShopId(ShopId shopId) {
        this.shopId = shopId;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
