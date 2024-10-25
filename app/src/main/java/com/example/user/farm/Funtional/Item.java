package com.example.user.farm.Funtional;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("CropID")
    @Expose
    private String cropID;


    @SerializedName("Count")
    @Expose
    private Integer count;


    @SerializedName("Photo")
    @Expose
    private String photo;


    @SerializedName("CropName")
    @Expose
    private String cropName;


    @SerializedName("Price")
    @Expose
    private String price;

    @SerializedName("Check")
    @Expose
    private Boolean Check;

    public Boolean getCheck() {
        return Check;
    }

    public void setCheck(Boolean check) {
        Check = check;
    }

    public String getCropID() {
        return cropID;
    }

    public void setCropID(String cropID) {
        this.cropID = cropID;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}