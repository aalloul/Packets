package com.app.shippy.android;

import android.provider.BaseColumns;

/**
 * Created by aalloul on 10/07/16.
 */
final class DataBaseContracts {
    // This is a helper class, it allows to define in one single place the datbase and its tables
    // Here each table is a subclass of DatabaseContracts. None of them can be instantiated
    // and they only have ``public static final'' variables.
    private DataBaseContracts() {}

    // types and separators
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT "; // can be used for date
    private static final String INTEGER_TYPE = " INTEGER "; // can be used for unix timestamp
    private static final String REAL_TYPE = " REAL ";
    private static final String BLOB_TYPE = " BLOB "; // can be used to store anything

     static abstract class Postmen implements BaseColumns {
         static final String TABLE_NAME = "postmen";
         static final String ROW_ID = "_id ";
         static final String ROW_ID_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";
         static final String COLUMN_NAME_NAME = "name";
         static final String COLUMN_NAME_RATING = "user_rating";
         static final String COLUMN_NAME_FIRSTNAME = "firstname";
         static final String COLUMN_NAME_PHONENUMBER = "phone_number";
         static final String COLUMN_NAME_PICTURE = "user_picture";

         static final String COLUMN_NAME_PICKUPADDRESS= "pickup_address";
         static final String COLUMN_NAME_PICKUPCITY = "pickup_city";
         static final String COLUMN_NAME_PICKUPCOUNTRY = "pickup_country";
         static final String COLUMN_NAME_PICKUPPOSTALCODE= "pickup_postal_code";
         static final String COLUMN_NAME_PICKUPLONGITUDE= "pickup_longitude";
         static final String COLUMN_NAME_PICKUPLATITUDE= "pickup_latitude";

         static final String COLUMN_NAME_DROPOFFADDRESS=  "dropoff_address";
         static final String COLUMN_NAME_DROPOFFCITY = "dropoff_city";
         static final String COLUMN_NAME_DROPOFFCOUNTRY = "dropoff_country";
         static final String COLUMN_NAME_DROPOFFPOSTALCODE=  "dropoff_postal_code";
         static final String COLUMN_NAME_DROPOFFLONGITUDE = "dropoff_longitude";
         static final String COLUMN_NAME_DROPOFFLATITUDE =  "dropoff_latitude";

         static final String COLUMN_NAME_USERCOMMENT = "user_comment";
         static final String COLUMN_NAME_SIZEPACKAGES = "packet_size";
         static final String COLUMN_NAME_NUMBERPACKAGES = "number_packages";
         static final String COLUMN_NAME_PICKUPDATE = "packet_pickup_date";
         static final String COLUMN_NAME_DROPOFFDATE = "packet_dropoff_date";
         static final String COLUMN_NAME_TRAVELBY= "package_travel_by";

         static final String CREATE_TABLE = "CREATE TABLE " +
                 TABLE_NAME + " (" +
                 ROW_ID + ROW_ID_TYPE + COMMA_SEP +
                 COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PHONENUMBER + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_RATING + INTEGER_TYPE + COMMA_SEP +
                 COLUMN_NAME_TRAVELBY + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICTURE + TEXT_TYPE + COMMA_SEP +

                 COLUMN_NAME_PICKUPADDRESS + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICKUPCITY + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICKUPCOUNTRY + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICKUPPOSTALCODE + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICKUPLONGITUDE + REAL_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICKUPLATITUDE + REAL_TYPE + COMMA_SEP +

                 COLUMN_NAME_DROPOFFADDRESS + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_DROPOFFCITY + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_DROPOFFCOUNTRY + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_DROPOFFPOSTALCODE + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_DROPOFFLONGITUDE + REAL_TYPE + COMMA_SEP +
                 COLUMN_NAME_DROPOFFLATITUDE + REAL_TYPE + COMMA_SEP +

                 COLUMN_NAME_USERCOMMENT + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_SIZEPACKAGES + TEXT_TYPE + COMMA_SEP +
                 COLUMN_NAME_PICKUPDATE + INTEGER_TYPE + COMMA_SEP +
                 COLUMN_NAME_DROPOFFDATE + INTEGER_TYPE + COMMA_SEP +
                 COLUMN_NAME_NUMBERPACKAGES + INTEGER_TYPE +
                 " )";

         static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

}

