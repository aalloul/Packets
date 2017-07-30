package com.app.shippy.android;


import android.graphics.Bitmap;

class TripOffer {
    private String firstName, surName, comment, phoneNumber, picture_str;
    private String pickupCity, pickupState, pickupCountry, pickupZipcode;
    private String dropoffCity, dropoffState, dropoffCountry, dropoffZipcode;
    private long pickupDate, dropoffDate;
    private int packageSize, numberPackages, travelBy;
    private Bitmap pictureBM;

    void setPicture_str(String picture_str) {
        this.picture_str = picture_str;
    }

    void setPictureBM(Bitmap pictureBM) {
        this.pictureBM = pictureBM;
    }

    String getPicture_str() {
    
        return picture_str;
    }

    Bitmap getPictureBM() {
        return pictureBM;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    void setSurName(String surName) {
        this.surName = surName;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    void setPickupState(String pickupState) {
        this.pickupState = pickupState;
    }

    void setPickupCountry(String pickupCountry) {
        this.pickupCountry = pickupCountry;
    }

    void setPickupZipcode(String pickupZipcode) {
        this.pickupZipcode = pickupZipcode;
    }

    void setDropoffCity(String dropoffCity) {
        this.dropoffCity = dropoffCity;
    }

    void setDropoffState(String dropoffState) {
        this.dropoffState = dropoffState;
    }

    void setDropoffCountry(String dropoffCountry) {
        this.dropoffCountry = dropoffCountry;
    }

    void setDropoffZipcode(String dropoffZipcode) {
        this.dropoffZipcode = dropoffZipcode;
    }

    void setPickupDate(long pickupDate) {
        this.pickupDate = pickupDate;
    }

    void setDropoffDate(long dropoffDate) {
        this.dropoffDate = dropoffDate;
    }

    void setPackageSize(int packageSize) {
        this.packageSize = packageSize;
    }

    void setNumberPackages(int numberPackages) {
        this.numberPackages = numberPackages;
    }

    void setTravelBy(int travelBy) {
        this.travelBy = travelBy;
    }

    String getFirstName() {
    
        return firstName;
    }

    String getSurName() {
        return surName;
    }

    String getComment() {
        return comment;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    String getPickupCity() {
        return pickupCity;
    }

    String getPickupState() {
        return pickupState;
    }

    String getPickupCountry() {
        return pickupCountry;
    }

    String getPickupZipcode() {
        return pickupZipcode;
    }

    String getDropoffCity() {
        return dropoffCity;
    }

    String getDropoffState() {
        return dropoffState;
    }

    String getDropoffCountry() {
        return dropoffCountry;
    }

    String getDropoffZipcode() {
        return dropoffZipcode;
    }

    long getPickupDate() {
        return pickupDate;
    }

    long getDropoffDate() {
        return dropoffDate;
    }

    int getPackageSize() {
        return packageSize;
    }

    int getNumberPackages() {
        return numberPackages;
    }

    int getTravelBy() {
        return travelBy;
    }

    TripOffer() {
        
    }
}
