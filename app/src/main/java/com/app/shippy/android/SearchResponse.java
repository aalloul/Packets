package com.app.shippy.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class SearchResponse {

    @SerializedName("userSurName")
    @Expose
    private String userSurName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userFirstName")
    @Expose
    private String userFirstName;
    @SerializedName("userPicture")
    @Expose
    private String userPicture;
    @SerializedName("userPhoneNumber")
    @Expose
    private String userPhoneNumber;
    @SerializedName("travels_by")
    @Expose
    private String travelsBy;
    @SerializedName("user_comment")
    @Expose
    private String userComment;
    @SerializedName("pickup_address")
    @Expose
    private String pickupAddress;
    @SerializedName("number_packages")
    @Expose
    private Integer numberPackages;
    @SerializedName("drop_off_postalCode")
    @Expose
    private String dropOffPostalCode;
    @SerializedName("drop_off_country")
    @Expose
    private String dropOffCountry;
    @SerializedName("pickup_longitude")
    @Expose
    private float pickupLongitude;
    @SerializedName("pickup_postalCode")
    @Expose
    private String pickupPostalCode;
    @SerializedName("pickup_city")
    @Expose
    private String pickupCity;
    @SerializedName("drop_off_longitude")
    @Expose
    private float dropOffLongitude;
    @SerializedName("pickup_country")
    @Expose
    private String pickupCountry;
    @SerializedName("size_packages")
    @Expose
    private String sizePackages;
    @SerializedName("drop_off_address")
    @Expose
    private String dropOffAddress;
    @SerializedName("pickup_latitude")
    @Expose
    private float pickupLatitude;
    @SerializedName("pickup_state")
    @Expose
    private String pickupState;
    @SerializedName("pickup_date")
    @Expose
    private long pickupDate;
    @SerializedName("drop_off_city")
    @Expose
    private String dropOffCity;
    @SerializedName("drop_off_latitude")
    @Expose
    private float dropOffLatitude;

    String getUserSurName() {
        return userSurName;
    }

    public void setUserSurName(String userSurName) {this.userSurName = userSurName;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {this.userFirstName = userFirstName;}

    String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {this.userPicture = userPicture;}

    String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {this.userPhoneNumber = userPhoneNumber;}

    String getTravelsBy() {
        return travelsBy;
    }

    public void setTravelsBy(String travelsBy) {this.travelsBy = travelsBy;}

    String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {this.userComment = userComment;}

    String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {this.pickupAddress = pickupAddress;}

    Integer getNumberPackages() {
        return numberPackages;
    }

    public void setNumberPackages(Integer numberPackages) {this.numberPackages = numberPackages;}

    String getDropOffPostalCode() {
        return dropOffPostalCode;
    }

    public void setDropOffPostalCode(String dropOffPostalCode) {this.dropOffPostalCode = dropOffPostalCode;}

    String getDropOffCountry() {
        return dropOffCountry;
    }

    public void setDropOffCountry(String dropOffCountry) {this.dropOffCountry = dropOffCountry;}

    float getPickupLongitude() {return pickupLongitude;}

    public void setPickupLongitude(float pickupLongitude) {this.pickupLongitude = pickupLongitude;}

    String getPickupPostalCode() {
        return pickupPostalCode;
    }

    public void setPickupPostalCode(String pickupPostalCode) {this.pickupPostalCode = pickupPostalCode;}

    String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    float getDropOffLongitude() {
        return dropOffLongitude;
    }

    public void setDropOffLongitude(float dropOffLongitude) {
        this.dropOffLongitude = dropOffLongitude;
    }

    String getPickupCountry() {
        return pickupCountry;
    }

    public void setPickupCountry(String pickupCountry) {
        this.pickupCountry = pickupCountry;
    }

    String getSizePackages() {
        return sizePackages;
    }

    public void setSizePackages(String sizePackages) {
        this.sizePackages = sizePackages;
    }

    String getDropOffAddress() {
        return dropOffAddress;
    }

    public void setDropOffAddress(String dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
    }

    float getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(float pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupState() {
        return pickupState;
    }

    public void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    long getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(long pickupDate) {
        this.pickupDate = pickupDate;
    }

    String getDropOffCity() {
        return dropOffCity;
    }

    public void setDropOffCity(String dropOffCity) {
        this.dropOffCity = dropOffCity;
    }

    float getDropOffLatitude() {
        return dropOffLatitude;
    }

    public void setDropOffLatitude(float dropOffLatitude) {
        this.dropOffLatitude = dropOffLatitude;
    }

    @Override
    public String toString() {
        return (this.getUserFirstName() + " going to " + this.pickupAddress);
    }
}