package com.app.shippy.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;

import java.util.HashMap;
import java.util.UUID;

class User {
    private String firstname;
    private String surname;
    private String picture_path;
    private String picture;
    private Bitmap pictureBM;
    private String phoneNumber;
    private int number_prompts;
    private boolean isRegistered;
    private static User mInstance = new User();
    private Context context;
    private String share_pref_file_name = "com.app.shippy.android.SharePrefUser";
    private String deviceId, deviceType;
    private LocationObject locationObject;

    private static final boolean DEBUG = true;

    void setContext (Context context) {
        this.context = context;
        this.set_user_details();
    }

    void saveDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(share_pref_file_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("deviceid", deviceId);
        editor.putString("devicetype", deviceType);
        editor.putInt("number_prompts", number_prompts);
        editor.putBoolean("isregistered", isRegistered);
        editor.putString("firstname", firstname);
        editor.putString("surname", surname);
        editor.putString("picture_path", picture_path);
        editor.putString("picture", picture);
        editor.putString("phone_number", phoneNumber);

        editor.putString("city", locationObject.getCity());
        editor.putString("address", locationObject.getAddress());
        editor.putString("state", locationObject.getState());
        editor.putString("country", locationObject.getCountry());
        editor.putString("zipcode", locationObject.getZipcode());
        editor.putFloat("longitude", locationObject.getLongitude());
        editor.putFloat("latitude", locationObject.getLatitude());
        editor.apply();
    }

    private void set_user_details(){
        HashMap<String, String> tmp = new HashMap<>();

        SharedPreferences sharedPreferences = context.getSharedPreferences(share_pref_file_name,
                Context.MODE_PRIVATE);

        this.setFirstname(sharedPreferences.getString("firstname", ""));
        this.setSurname(sharedPreferences.getString("surname", ""));
        this.setPhoneNumber(sharedPreferences.getString("phone_number", ""));

        this.setNumber_prompts(sharedPreferences.getInt("number_prompts", 0));
        this.setRegistered(sharedPreferences.getBoolean("isregistered", false));
        this.setDeviceId(sharedPreferences.getString("deviceid", ""));
        this.setDeviceType(sharedPreferences.getString("devicetype", ""));
        this.setPicturePath(sharedPreferences.getString("picture_path", ""));
        this.setPicture(sharedPreferences.getString("picture", ""));
        this.setPictureBM(Utilities.StringToBitMap(sharedPreferences.getString("picture", "")));

        locationObject.setCity(sharedPreferences.getString("city", ""));
        locationObject.setCountry(sharedPreferences.getString("country", ""));
        locationObject.setAddress(sharedPreferences.getString("address", ""));
        locationObject.setZipcode(sharedPreferences.getString("zipcode", ""));
        locationObject.setLatitude(sharedPreferences.getFloat("latitude", 0));
        locationObject.setLongitude(sharedPreferences.getFloat("longitude", 0));
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String getPhoneNumber() {

        return phoneNumber;
    }

    void setPicture(String picture) {
        this.picture = picture;
    }

    String getPicture() {return picture;}

    Bitmap getPictureBM() {
        return pictureBM;
    }

    void setPictureBM(Bitmap pictureBM) {
        this.pictureBM = pictureBM;
    }

    String getPicturePath() {
        return picture_path;
    }

    void setPicturePath(String picture_path) {this.picture_path = picture_path;}

    private String generateDeviceId() {
        return UUID.randomUUID().toString();
    }

    private String generateDeviceType() {
        return Build.DEVICE;
    }

    private void setDeviceId(String deviceId) {
        if (deviceId.equals("")) {
            this.deviceId = generateDeviceId();
        } else {
            this.deviceId = deviceId;
        }
    }

    private void setDeviceType(String deviceType) {
        if (deviceType.equals("")) {
            this.deviceType = generateDeviceType();
        } else {
            this.deviceType = deviceType;
        }
    }

    private void setNumber_prompts(int number_prompts) {
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

    String getDeviceId() {return deviceId;}

    String getDeviceType() {return deviceType;}

    int getNumber_prompts() {

        return number_prompts;
    }

    boolean isRegistered() {
        if (DEBUG) return false;
        return isRegistered;
    }

    String getSurname() {
        return surname;
    }

    String getFirstname() {
        return firstname;
    }

    HashMap<String, Object> getHashMapRepresentation() {
        HashMap<String, Object> tmp = new HashMap<>();

        tmp.put("Firstname", firstname);
        tmp.put("Surname", surname);
        tmp.put("PhoneNumber", phoneNumber);
        tmp.put("Picture", picture);

        return tmp;
    }

    LocationObject getLocationObject() {
        return locationObject;
    }

    void updateLocation(LocationObject locationObject) {
        this.locationObject = locationObject;
    }

    static User getInstance() {
        // We want this class to be a singleton
        if (mInstance == null) {mInstance = new User();}
        return mInstance;
    }

    private User () {
        locationObject = new LocationObject();
    }

}
