package com.app.shippy.android;

import java.util.HashMap;

/**
 * Created by adamalloul on 22/07/2017.
 */

class LocationObject {
    private String address, city, state, country, zipcode;
    private float longitude;
    private float latitude;

    void setAddress(String address) {
        this.address = address;
    }

    void setCity(String city) {
        this.city = city;
    }

    void setState(String state) {
        this.state = state;
    }

    void setCountry(String country) {
        this.country = country;
    }

    void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    String getAddress() {

        return address;
    }

    String getCity() {
        return city;
    }

    String getState() {
        return state;
    }

    String getCountry() {
        return country;
    }

    String getZipcode() {
        return zipcode;
    }

    float getLongitude() {
        return longitude;
    }

    float getLatitude() {
        return latitude;
    }

    HashMap<String, Object> getHashMapRepresentation(String prefix) {
        HashMap<String, Object> tmp = new HashMap<>();
        tmp.put(prefix + "Address", address);
        tmp.put(prefix + "City", city);
        tmp.put(prefix + "State", state);
        tmp.put(prefix + "Country", country);
        tmp.put(prefix + "Zipcode", zipcode);
        tmp.put(prefix + "Latitude", latitude);
        tmp.put(prefix + "Longitude", longitude);
        return tmp;
    }

    LocationObject(HashMap<String, String> location) {

        setAddress(location.get("address"));
        setCity(location.get("city"));
        setState(location.get("state"));
        setCountry(location.get("country"));
        setZipcode(location.get("zipcode"));
        setLatitude(Float.valueOf(location.get("latitude")));
        setLongitude(Float.valueOf(location.get("longitude")));
    }

    LocationObject(LocationObject locationObject) {
        setAddress(locationObject.getAddress());
        setCity(locationObject.getCity());
        setState(locationObject.getState());
        setCountry(locationObject.getCountry());
        setZipcode(locationObject.getZipcode());
        setLatitude(locationObject.getLatitude());
        setLongitude(locationObject.getLongitude());
    }

    LocationObject() {
        address = "";
        city = "";
        state = "";
        country = "";
        zipcode = "";
        longitude = 0;
        latitude = 0;
    }
}
