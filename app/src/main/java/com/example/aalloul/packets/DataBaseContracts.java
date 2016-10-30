package com.example.aalloul.packets;

import android.provider.BaseColumns;

/**
 * Created by aalloul on 10/07/16.
 */
final class DataBaseContracts {
    // This is a helper class, it allows to define in one single place the datbase and its tables
    // Here each table is a subclass of DatabaseContracts. None of them can be instantiated
    // and they only have ``public static final'' variables.
    private DataBaseContracts() {};

    // types and separators
    public static final String COMMA_SEP = ",";
    public static final String TEXT_TYPE = " TEXT "; // can be used for date
    public static final String INTEGER_TYPE = " INTEGER "; // can be used for unix timestamp
    public static final String REAL_TYPE = " REAL ";
    public static final String BLOB_TYPE = " BLOB "; // can be used to store anything

    // Table defining the customer private data
    public static abstract class UserData implements BaseColumns {
        public static final String TABLE_NAME = "userdata";
        public static final String ROW_ID = "_id ";
        public static final String ROW_ID_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";

        // user details
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_PHONENUMBER = "phone_number";



        // Queries
        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                ROW_ID + ROW_ID_TYPE + COMMA_SEP +
                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_STREET + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PHONENUMBER + TEXT_TYPE +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    /* Inner class that defines the table contents */
    public static abstract class Senders implements BaseColumns {
        public static final String TABLE_NAME = "senders";
        public static final String ROW_ID = "_id ";
        public static final String ROW_ID_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";

        // user details
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_PHONENUMBER = "phone_number";


        // package description
        public static final String COLUMN_NAME_PACKETDESTINATION = "packet_destination";
        public static final String COLUMN_NAME_PACKETSOURCE = "packet_source";
        public static final String COLUMN_NAME_PACKETDESCRIPTION = "packet_description";
        public static final String COLUMN_NAME_SENDBYDATE = "packet_send_by_date";
        public static final String COLUMN_NAME_NUMBERPACKAGES = "packet_number_packages";

        // Queries
        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                ROW_ID + ROW_ID_TYPE + COMMA_SEP +
                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_STREET + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PHONENUMBER + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PACKETDESTINATION + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PACKETSOURCE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PACKETDESCRIPTION + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_SENDBYDATE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_NUMBERPACKAGES + INTEGER_TYPE +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class Postmen implements BaseColumns {
        public static final String TABLE_NAME = "postmen";
        public static final String ROW_ID = "_id ";
        public static final String ROW_ID_TYPE = " INTEGER PRIMARY KEY AUTOINCREMENT";

        // user details
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STREET = "street";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_PHONENUMBER = "phone_number";
        public static final String COLUMN_NAME_RATING = "user_rating";

        // package description
        public static final String COLUMN_NAME_SOURCECITY = "source_city";
        public static final String COLUMN_NAME_SOURCECOUNTRY = "source_country";
        public static final String COLUMN_NAME_SOURCESTREET = "source_street";


        public static final String COLUMN_NAME_DESTINATIONCITY = "destination_city";
        public static final String COLUMN_NAME_DESTINATIONCOUNTRY = "destination_country";
        public static final String COLUMN_NAME_DESTINATIONSTREET = "destination_street";

        public static final String COLUMN_NAME_POSTMANCOMMENT = "transport_comment";
        public static final String COLUMN_NAME_SIZEPACKAGES = "packet_size";
        public static final String COLUMN_NAME_NUMBERPACKAGES = "number_packages";

        public static final String COLUMN_NAME_TAKEBYDATE = "packet_take_by_date";
        public static final String COLUMN_NAME_DELIVERBYDATE = "packet_deliver_by_date";
        public static final String COLUMN_NAME_TRANSPORTMETHOD = "package_transport_method";

        // Queries
        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                // User specific information
                ROW_ID + ROW_ID_TYPE + COMMA_SEP +
                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_FIRSTNAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_CITY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_STREET + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_COUNTRY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_PHONENUMBER + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_RATING + INTEGER_TYPE + COMMA_SEP +
                // Details of the offer
                COLUMN_NAME_SOURCECITY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_SOURCECOUNTRY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_SOURCESTREET + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_TRANSPORTMETHOD + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_DESTINATIONCITY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_DESTINATIONCOUNTRY + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_DESTINATIONSTREET + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_POSTMANCOMMENT + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_SIZEPACKAGES + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_TAKEBYDATE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_DELIVERBYDATE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_NUMBERPACKAGES + INTEGER_TYPE +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

}

