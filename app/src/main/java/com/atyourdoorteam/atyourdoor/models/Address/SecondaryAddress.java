package com.atyourdoorteam.atyourdoor.models.Address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecondaryAddress {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("postalCode")
    @Expose
    private String postalCode;
    @SerializedName("state")
    @Expose
    private String state;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
