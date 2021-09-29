package com.atyourdoorteam.atyourdoor.models;

import com.atyourdoorteam.atyourdoor.models.Address.PrimaryAddress;
import com.atyourdoorteam.atyourdoor.models.Address.SecondaryAddress;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class UserInfo {
    @SerializedName("primaryAddress")
    @Expose
    private PrimaryAddress primaryAddress;
    @SerializedName("secondaryAddress")
    @Expose
    private SecondaryAddress secondaryAddress;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("credit")
    @Expose
    private String credit;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("number")
    @Expose
    private BigInteger number;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public PrimaryAddress getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(PrimaryAddress primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public SecondaryAddress getSecondaryAddress() {
        return secondaryAddress;
    }

    public void setSecondaryAddress(SecondaryAddress secondaryAddress) {
        this.secondaryAddress = secondaryAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
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

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
