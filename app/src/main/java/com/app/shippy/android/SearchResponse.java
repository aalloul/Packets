package com.app.shippy.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class SearchResponse {

    @SerializedName("Surname")
    @Expose
    private String Surname;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Firstname")
    @Expose
    private String Firstname;
    @SerializedName("Picture")
    @Expose
    private String Picture;
    @SerializedName("PhoneNumber")
    @Expose
    private String PhoneNumber;
    @SerializedName("TravelBy")
    @Expose
    private String TravelBy;
    @SerializedName("Comment")
    @Expose
    private String Comment;
    @SerializedName("PickupAddress")
    @Expose
    private String PickupAddress;
    @SerializedName("NumberPackages")
    @Expose
    private Integer NumberPackages;
    @SerializedName("DropoffZipcode")
    @Expose
    private String DropoffZipcode;
    @SerializedName("DropoffCountry")
    @Expose
    private String DropoffCountry;
    @SerializedName("PickupLongitude")
    @Expose
    private float PickupLongitude;
    @SerializedName("PickupZipcode")
    @Expose
    private String PickupZipcode;
    @SerializedName("PickupCity")
    @Expose
    private String PickupCity;
    @SerializedName("DropoffLongitude")
    @Expose
    private float DropoffLongitude;
    @SerializedName("PickupCountry")
    @Expose
    private String PickupCountry;
    @SerializedName("PackageSize")
    @Expose
    private String PackageSize;
    @SerializedName("DropoffAddress")
    @Expose
    private String DropoffAddress;
    @SerializedName("PickupLatitude")
    @Expose
    private float PickupLatitude;
    @SerializedName("PickupState")
    @Expose
    private String PickupState;
    @SerializedName("PickupDate")
    @Expose
    private long PickupDate;
    @SerializedName("DropoffCity")
    @Expose
    private String DropoffCity;
    @SerializedName("DropoffLatitude")
    @Expose
    private float DropoffLatitude;

    String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {this.Surname = surname;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {this.Firstname = firstname;}

    String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {this.Picture = picture;}

    String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {this.PhoneNumber = phoneNumber;}

    String getTravelBy() {
        return TravelBy;
    }

    public void setTravelBy(String travelBy) {this.TravelBy = travelBy;}

    String getComment() {
        return Comment;
    }

    public void setComment(String comment) {this.Comment = comment;}

    String getPickupAddress() {
        return PickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {this.PickupAddress = pickupAddress;}

    Integer getNumberPackages() {
        return NumberPackages;
    }

    public void setNumberPackages(Integer numberPackages) {this.NumberPackages = numberPackages;}

    String getDropoffZipcode() {
        return DropoffZipcode;
    }

    public void setDropoffZipcode(String dropoffZipcode) {this.DropoffZipcode = dropoffZipcode;}

    String getDropoffCountry() {
        return DropoffCountry;
    }

    public void setDropoffCountry(String dropoffCountry) {this.DropoffCountry = dropoffCountry;}

    float getPickupLongitude() {return PickupLongitude;}

    public void setPickupLongitude(float pickupLongitude) {this.PickupLongitude = pickupLongitude;}

    String getPickupZipcode() {
        return PickupZipcode;
    }

    public void setPickupZipcode(String pickupZipcode) {this.PickupZipcode = pickupZipcode;}

    String getPickupCity() {
        return PickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.PickupCity = pickupCity;
    }

    float getDropoffLongitude() {
        return DropoffLongitude;
    }

    public void setDropoffLongitude(float dropoffLongitude) {
        this.DropoffLongitude = dropoffLongitude;
    }

    String getPickupCountry() {
        return PickupCountry;
    }

    public void setPickupCountry(String pickupCountry) {
        this.PickupCountry = pickupCountry;
    }

    String getPackageSize() {
        return PackageSize;
    }

    public void setPackageSize(String packageSize) {
        this.PackageSize = packageSize;
    }

    String getDropoffAddress() {
        return DropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.DropoffAddress = dropoffAddress;
    }

    float getPickupLatitude() {
        return PickupLatitude;
    }

    public void setPickupLatitude(float pickupLatitude) {
        this.PickupLatitude = pickupLatitude;
    }

    public String getPickupState() {
        return PickupState;
    }

    public void setPickupState(String pickupState) {
        this.PickupState = pickupState;
    }

    long getPickupDate() {
        return PickupDate;
    }

    public void setPickupDate(long pickupDate) {
        this.PickupDate = pickupDate;
    }

    String getDropoffCity() {
        return DropoffCity;
    }

    public void setDropoffCity(String dropoffCity) {
        this.DropoffCity = dropoffCity;
    }

    float getDropoffLatitude() {
        return DropoffLatitude;
    }

    public void setDropoffLatitude(float dropoffLatitude) {
        this.DropoffLatitude = dropoffLatitude;
    }

    @Override
    public String toString() {
        return (this.getFirstname() + " going to " + this.PickupAddress);
    }
}