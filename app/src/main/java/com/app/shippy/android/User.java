package com.app.shippy.android;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.HashMap;
import java.util.UUID;

class User {
    private String firstname, surname;
    private String city, state, country, address, zipcode;
    private int number_prompts;
    private boolean isRegistered;
    private static User mInstance = new User();
    private Context context;
    private String share_pref_file_name = "com.app.shippy.android.SharePrefUser";
    private String deviceId, deviceType;

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
    }

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

    public void setNumber_prompts(int number_prompts) {
        this.number_prompts = number_prompts+1;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void setFirstname(String firstname) {

        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipcode(String zipcode) {
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


    public String getDeviceId() {return deviceId;}

    public String getDeviceType() {return deviceType;}

    static synchronized User getInstance() {
        // We want this class to be a singleton
        if (mInstance == null) {mInstance = new User();}
        return mInstance;
    }

    public int getNumber_prompts() {

        return number_prompts;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getSurname() {
        return surname;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getFirstname() {
        return firstname;
    }
}
