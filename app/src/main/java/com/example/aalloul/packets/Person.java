package com.example.aalloul.packets;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by aalloul on 03/07/16.
 */
abstract class Person extends Activity {
    //TODO Implement logic to update the table

    JSONObject thisUser = new JSONObject();
    private Context localContext;


    // Constructor with all variables provided
    public Person(Context ctx, JSONObject userDeclaration) {
        localContext = ctx;
        thisUser = userDeclaration;

    }

    public Person(Context ctx){
        localContext = ctx;
    }

//    public Person(Context ctx, ArrayList<String> listArgs){
//        this(ctx,listArgs.get(1), listArgs.get(2), listArgs.get(3), listArgs.get(4), listArgs.get(5),
//                listArgs.get(6), listArgs.get(7), listArgs.get(8), Integer.parseInt(listArgs.get(9)),
//                listArgs.get(10), Integer.parseInt(listArgs.get(11)),
//                Integer.parseInt(listArgs.get(12)), Integer.parseInt(listArgs.get(13)),
//                listArgs.get(14), listArgs.get(15));
//
//    }



    // Retrieve user details from the SQL database
    protected abstract Cursor getUsers();


    // Getters

    // This is useful for when we want to initialize with a list of arguments.
    // For example, when users are retrieved in one batch from SQLite
//    protected ArrayList<String> getAllInformation(){
//        ArrayList<String> tmp =  new ArrayList<String>(Arrays.asList(name, firstName, sourceCity,
//                sourceCountry, sourceStreet, destCity, destCountry, destStreet,
//                Integer.toString(numberPackages), sizePackage, Integer.toString(departureDate),
//                Integer.toString(arrivalDate), Integer.toString(rating), comment, phoneNumber));
//
//        return tmp;
//    }

    protected String getName() throws JSONException {
        if (thisUser.isNull("name")) return "";
        return thisUser.getString("name");
    }

    protected String getFirstName() throws JSONException {
        if (thisUser.isNull("firstName")) return "";
        return thisUser.getString("firstName");
    }

    protected String getSourceCity() throws JSONException {
        if (thisUser.isNull("sourceCity")) return "";
        return thisUser.getString("sourceCity");
    }

    protected String getSourceCountry() throws JSONException {
        if (thisUser.isNull("sourceCountry")) return "";
        return thisUser.getString("sourceCountry");
    }

    protected String getSourceStreet() throws JSONException {
        if (thisUser.isNull("sourceStreet")) return "";
        return thisUser.getString("sourceStreet");
    }

    protected String getDestCity() throws JSONException {
        if (thisUser.isNull("destCity")) return "";
        return thisUser.getString("destCity");
    }

    protected String getDestCountry() throws JSONException {
        if (thisUser.isNull("destCountry")) return "";
        return thisUser.getString("destCountry");
    }

    protected String getDestStreet() throws JSONException {
        if (thisUser.isNull("destStreet")) return "";
        return thisUser.getString("destStreet");
    }

    protected int getNumberPackages() throws JSONException {
        if (thisUser.isNull("numberPackages")) return 0;
        return thisUser.getInt("numberPackages");
    }

    protected String getSizePackage() throws JSONException {
        if (thisUser.isNull("sizePackage")) return "";
        return thisUser.getString("sizePackage");
    }

    protected int getDepartureDate() throws JSONException {
        if (thisUser.isNull("departureDate")) return 0;
        return thisUser.getInt("departureDate");
    }

    protected int getArrivalDate() throws JSONException {
        if (thisUser.isNull("arrivalDate")) return 0;
        return thisUser.getInt("arrivalDate");
    }

    protected int getRating() throws JSONException {
        if (thisUser.isNull("rating")) return 0;
        return thisUser.getInt("rating");
    }

    protected String getComment() throws JSONException {
        if (thisUser.isNull("comment")) return "";
        return thisUser.getString("comment");
    }

    protected String getPhoneNumber() throws JSONException {
        if (thisUser.isNull("phoneNumber")) return "";
        return thisUser.getString("phoneNumber");
    }

    protected String getTransportMethod() throws JSONException {
        if (thisUser.isNull("transportmethod")) return "";
        return thisUser.getString("transportmethod");
    }

    protected Context getLocalContext() throws JSONException{
        return localContext;
    }

}
