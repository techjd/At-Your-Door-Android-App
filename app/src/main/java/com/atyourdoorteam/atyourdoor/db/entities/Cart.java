package com.atyourdoorteam.atyourdoor.db.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class Cart implements Parcelable {
    @PrimaryKey @NonNull
    @ColumnInfo(name = "productId")
    private String productId;
    @ColumnInfo(name = "shopId")
    private String shopId;
    @ColumnInfo(name = "mainCategoryId")
    private String mainCategoryId;
    @ColumnInfo(name = "subCategoryId")
    private String subCategoryId;
    @ColumnInfo(name = "productImageURL")
    private String productImageURL;
    @ColumnInfo(name = "productName")
    private String productName;
    @ColumnInfo(name = "productPrice")
    private int productPrice;
    @ColumnInfo(name = "productQuantity")
    private int productQuantity;


    @Ignore
    public Cart() {
    }



    public Cart(String productId, String shopId, String mainCategoryId, String subCategoryId, String productImageURL, String productName, int productPrice, int productQuantity) {
        this.productId = productId;
        this.shopId = shopId;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryId = subCategoryId;
        this.productImageURL = productImageURL;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    protected Cart(Parcel in) {
        productId = in.readString();
        shopId = in.readString();
        mainCategoryId = in.readString();
        subCategoryId = in.readString();
        productImageURL = in.readString();
        productName = in.readString();
        productPrice = in.readInt();
        productQuantity = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    @NonNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(shopId);
        dest.writeString(mainCategoryId);
        dest.writeString(subCategoryId);
        dest.writeString(productImageURL);
        dest.writeString(productName);
        dest.writeInt(productPrice);
        dest.writeInt(productQuantity);
    }
}
