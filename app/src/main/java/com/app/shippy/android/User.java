package com.app.shippy.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.HashMap;
import java.util.UUID;

class User {
    private String firstname;
    private String surname;
    private String picture_path;
    private String picture;
    private String phoneNumber;
    private String city, state, country, address, zipcode;
    private int number_prompts;
    private boolean isRegistered;
    private static User mInstance = new User();
    private Context context;
    private String share_pref_file_name = "com.app.shippy.android.SharePrefUser";
    private String deviceId, deviceType;
    private static final boolean DEBUG = true;

    private User () {

    }

    void setContext (Context context) {
        this.context = context.getApplicationContext();
        this.set_user_details();
    }

    void set_user_details(){
        HashMap<String, String> tmp = new HashMap<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(share_pref_file_name,
                Context.MODE_PRIVATE);

        this.setFirstname(sharedPreferences.getString("firstname", ""));
        this.setSurname(sharedPreferences.getString("surname", ""));
        this.setCity(sharedPreferences.getString("city", ""));
        this.setCountry(sharedPreferences.getString("country", ""));
        this.setAddress(sharedPreferences.getString("address", ""));
        this.setZipcode(sharedPreferences.getString("zipcode", ""));
        this.setNumber_prompts(sharedPreferences.getInt("number_prompts", 0));
        this.setRegistered(sharedPreferences.getBoolean("isregistered", false));
        this.setDeviceId(sharedPreferences.getString("deviceid", ""));
        this.setDeviceType(sharedPreferences.getString("devicetype", ""));
        this.setState(sharedPreferences.getString("state", ""));
        this.setPicturePath(sharedPreferences.getString("picture", ""));
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String getPhoneNumber() {

        return phoneNumber;
    }

    void setPicture(String picture) {this.picture = picture;}

    String getPicture() {return picture;}

    public String getPicturePath() {
        return picture_path;
    }

    void setPicturePath(String picture_path) {this.picture_path = picture_path;}

    void setState(String state) {this.state = state;}

    String getState() {return state;}

    private String generateDeviceId() {
        return UUID.randomUUID().toString();
    }

    private String generateDeviceType() {
        return Build.DEVICE;
    }

    void setDeviceId(String deviceId) {
        if (deviceId.equals("")) {
            this.deviceId = generateDeviceId();
        } else {
            this.deviceId = deviceId;
        }
    }

    void setDeviceType(String deviceType) {
        if (deviceType.equals("")) {
            this.deviceType = generateDeviceType();
        } else {
            this.deviceType = deviceType;
        }
    }

    void setNumber_prompts(int number_prompts) {
        this.number_prompts = number_prompts+1;
    }

    void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    void setFirstname(String firstname) {

        this.firstname = firstname;
    }

    void setSurname(String surname) {
        this.surname = surname;
    }

    void setCity(String city) {
        this.city = city;
    }

    void setCountry(String country) {
        this.country = country;
    }

    void setAddress(String address) {
        this.address = address;
    }

    void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    void saveDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_pref_file_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("state", state);
        editor.putString("deviceid", deviceId);
        editor.putString("devicetype", deviceType);
        editor.putInt("number_prompts", number_prompts);
        editor.putBoolean("isregistered", isRegistered);
        editor.putString("firstname", firstname);
        editor.putString("surname", surname);
        editor.putString("city", city);
        editor.putString("country", country);
        editor.putString("address", address);
        editor.putString("zipcode", zipcode);
        editor.apply();
    }


    String getDeviceId() {return deviceId;}

    String getDeviceType() {return deviceType;}

    static synchronized User getInstance() {
        // We want this class to be a singleton
        if (mInstance == null) {mInstance = new User();}
        return mInstance;
    }

    public int getNumber_prompts() {

        return number_prompts;
    }

    boolean isRegistered() {
        if (DEBUG) return false;
        return isRegistered;
    }

    String getSurname() {
        return surname;
    }

    String getCity() {
        return city;
    }

    String getCountry() {
        return country;
    }

    String getAddress() {
        return address;
    }

    String getZipcode() {
        return zipcode;
    }

    String getFirstname() {
        return firstname;
    }

    HashMap<String, String> getLocationObject() {
        HashMap<String, String> tmp = new HashMap<>();
        tmp.put(context.getResources().getString(R.string.saved_user_city), this.city);
        tmp.put(context.getResources().getString(R.string.saved_user_country), this.country);
        tmp.put(context.getResources().getString(R.string.saved_user_postalcode), this.zipcode);
        tmp.put(context.getResources().getString(R.string.saved_user_address), this.address);
        return tmp;
    }
}
