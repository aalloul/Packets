package com.app.shippy.android;


import android.content.Context;
import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;

class TripRequestDetails {
    
    private String[] package_size_str;
    private int package_size_int=1;
    private int number_packages=1;
    private String comment;
    private int travelBy = 1;
    private long pickup_date, dropoff_date;
    private LocationObject dropoffLocation, pickupLocation; 
    private static Context context;
    
    void setContext(Context context) {this.context = context;}
    
    void setPackage_size_str() {
        this.package_size_str = new String[]{"Small", "Medium", "Large"};
    }

    void setPackage_size_int(int package_size_int) {
        this.package_size_int = package_size_int;
    }

    void setNumber_packages(int number_packages) {
        this.number_packages = number_packages;
    }

    void setComment(String comment) {
        this.comment = comment;
    }

    void setPickup_date(long pickup_date) {
        this.pickup_date = pickup_date;
    }

    void setPickup_date(String date) {
        pickup_date = Utilities.Date2EpochMillis(date, "yyyy-MM-dd");
    }

    void setDropoff_date(long dropoff_date) {
        this.dropoff_date = dropoff_date;
    }

    String getPackage_size_str() {

        return package_size_str[package_size_int];
    }

    int getPackage_size_int() {
        return package_size_int;
    }

    int getNumber_packages() {
        return number_packages;
    }

    String getComment() {
        return comment;
    }

    long getPickup_date() {
        return pickup_date;
    }

    long getDropoff_date() {
        return dropoff_date;
    }

    void setDropoffLocation(LocationObject dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    void setPickupLocation(LocationObject pickupLocation) {
        this.pickupLocation = new LocationObject(pickupLocation);
    }

    void setTravelBy(int travelBy) {
        /* This integer represents the position in the array
        travelling_by in the strings assets.
         */
        this.travelBy = travelBy;
    }

    HashMap<String, Object> getHashMapRepresentation() {
        HashMap<String, Object> tmp = new HashMap<>();
        tmp.put("Comment", comment);
        tmp.put("PackageSize", package_size_int);
        tmp.put("PickupDate", pickup_date);
        tmp.put("NumberPackages", number_packages);
        tmp.put("TravelBy", travelBy);
        tmp.putAll(dropoffLocation.getHashMapRepresentation("Dropoff"));
        tmp.putAll(pickupLocation.getHashMapRepresentation("Pickup"));

        return tmp;
    }

    LocationObject getDropoffLocation() {
    
        return dropoffLocation;
    }

    LocationObject getPickupLocation() {
        return pickupLocation;
    }

    private static final TripRequestDetails ourInstance = new TripRequestDetails();
    static TripRequestDetails getInstance() {
        return ourInstance;
    }

    private TripRequestDetails() {
        pickup_date = Utilities.CurrentTimeMS() + 24*3600*1000;
        dropoffLocation = new LocationObject();
        pickupLocation = new LocationObject();
    }
    
}
