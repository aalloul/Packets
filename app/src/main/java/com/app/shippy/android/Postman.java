package com.app.shippy.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.app.shippy.android.DataBaseContracts.Postmen;
import java.util.Arrays;
import android.util.Log;

/**
 * Created by aalloul on 03/07/16.
 */
class Postman {
    // Logging
    private final static String LOG_TAG = "Postman";
    private final static boolean DEBUG = false;
    // Some variables
    private PacketsDatabase packetsDatabase;


    Postman (Context ctx) {
//        super(ctx);
        packetsDatabase = new PacketsDatabase(ctx);
    }

    // Gets the results from the back-end and stores them into the DB
    Postman (Context ctx, SearchResponse[] searchResults) {
//        super(ctx);
        if (DEBUG) Log.i(LOG_TAG, "Postman - Got search results");
        packetsDatabase = new PacketsDatabase(ctx);
        this.updateTable(packetsDatabase.getWritableDatabase(),searchResults);
    }

    // Method that returns the whole content of the SQLite table.
//    @Override
    protected Cursor getUsers(){
        if (DEBUG) Log.i(LOG_TAG, " getUsers -- Start getting all postmen from SQLite");

        // Query the SQLite db
        SQLiteDatabase db = packetsDatabase.getReadableDatabase();

        Cursor c = db.query(
                Postmen.TABLE_NAME,  // The table to query
                null,                // The columns to return
                null,                // The columns for the WHERE clause
                null,                // The values for the WHERE clause
                null,                // don't group the rows
                null,                // don't filter by row groups
                null                 // The sort order
        );

        if (DEBUG) Log.i(LOG_TAG, " getUsers -- Return all postmen from SQLite");

        return c;
    }

    Cursor get_Data_For_ListView() {
        // TODO if no values returned, widen date range and/or request update of db... tbd outside
        // TODO arrange the ordering of the returned results -- currently ordered by date
        // TODO if backend throws exception, forward it to the main caller.
        // TODO base64 encode/decode of pictures

        if (DEBUG) Log.i(LOG_TAG, "get_Data_For_ListView -- Start getting all postmen for ListView");
        SQLiteDatabase db = packetsDatabase.getReadableDatabase();
        Cursor c;

        // Columns to retrieve
        String[] projection = {
                Postmen.ROW_ID, Postmen.COLUMN_NAME_FIRSTNAME, Postmen.COLUMN_NAME_NUMBERPACKAGES,
                Postmen.COLUMN_NAME_PICKUPCITY, Postmen.COLUMN_NAME_PICKUPCOUNTRY,
                Postmen.COLUMN_NAME_DROPOFFCITY, Postmen.COLUMN_NAME_DROPOFFCOUNTRY,
                Postmen.COLUMN_NAME_PICKUPDATE, Postmen.COLUMN_NAME_DROPOFFDATE,
                Postmen.COLUMN_NAME_USERCOMMENT, Postmen.COLUMN_NAME_PHONENUMBER,
                Postmen.COLUMN_NAME_SIZEPACKAGES, Postmen.COLUMN_NAME_TRAVELBY,
                Postmen.COLUMN_NAME_PICTURE};

        // Sort order
        String sortOrder = Postmen.COLUMN_NAME_PICKUPDATE+ " ASC";

        // The query itself
        c = db.query(Postmen.TABLE_NAME,  // The table to query
                projection,          // The columns to return
                null,           // The columns for the WHERE clause
                null,     // The values for the WHERE clause
                null,                // don't group the rows
                null,                // don't filter by row groups
                sortOrder            // The sort order
        );

        if (DEBUG) Log.i(LOG_TAG, "get_Data_For_ListView - The actual query returned "+c.getCount()+" entries");
        return c;

    }

    private void updateTable (SQLiteDatabase db, SearchResponse[] queryResults) {
        if (DEBUG) Log.i(LOG_TAG,"updateTable - Start");
//        new ArrayList<String>(Arrays.asList("*"));
        db.execSQL("delete from "+ Postmen.TABLE_NAME);

        if (queryResults == null || queryResults.length == 0) return;

        try {
            if (DEBUG) Log.i(LOG_TAG, "updateTable - received "+ queryResults.length +" new entries");

            for (SearchResponse q:queryResults) {
                ContentValues values = new ContentValues();
                values.put(Postmen.COLUMN_NAME_NAME , q.getUserSurName());
                values.put(Postmen.COLUMN_NAME_FIRSTNAME , q.getUserFirstName());
                values.put(Postmen.COLUMN_NAME_PHONENUMBER , q.getUserPhoneNumber());
                values.put(Postmen.COLUMN_NAME_PICTURE, q.getUserPicture());
                values.put(Postmen.COLUMN_NAME_USERCOMMENT , q.getUserComment());
//                values.put(Postmen.COLUMN_NAME_RATING , q.getUser_rating;

                values.put(Postmen.COLUMN_NAME_PICKUPLATITUDE, q.getPickupLatitude());
                values.put(Postmen.COLUMN_NAME_PICKUPLONGITUDE, q.getPickupLongitude());
                values.put(Postmen.COLUMN_NAME_PICKUPCITY, q.getPickupCity());
                values.put(Postmen.COLUMN_NAME_PICKUPADDRESS, q.getPickupAddress());
                values.put(Postmen.COLUMN_NAME_PICKUPCOUNTRY, q.getPickupCountry());
                values.put(Postmen.COLUMN_NAME_PICKUPPOSTALCODE, q.getPickupPostalCode());
                values.put(Postmen.COLUMN_NAME_DROPOFFLATITUDE, q.getDropOffLatitude());
                values.put(Postmen.COLUMN_NAME_DROPOFFLONGITUDE, q.getDropOffLongitude());
                values.put(Postmen.COLUMN_NAME_DROPOFFCITY , q.getDropOffCity());
                values.put(Postmen.COLUMN_NAME_DROPOFFCOUNTRY , q.getDropOffCountry());
                values.put(Postmen.COLUMN_NAME_DROPOFFADDRESS, q.getDropOffAddress());
                values.put(Postmen.COLUMN_NAME_DROPOFFPOSTALCODE, q.getDropOffPostalCode());

                values.put(Postmen.COLUMN_NAME_SIZEPACKAGES , q.getSizePackages());
                values.put(Postmen.COLUMN_NAME_NUMBERPACKAGES , q.getNumberPackages());
                values.put(Postmen.COLUMN_NAME_TRAVELBY, q.getTravelsBy());
                values.put(Postmen.COLUMN_NAME_PICKUPDATE , q.getPickupDate());
//                values.put(Postmen.COLUMN_NAME_DROPOFFDATE , q.getDropoffDate());

                long mylong = db.insert(Postmen.TABLE_NAME,"",values);
                if (DEBUG) Log.i(LOG_TAG,"updateTable - db.insert returned "+mylong);

            }

        } catch (Exception e){
            if (DEBUG) Log.e(LOG_TAG, "updateTable - An unknown error happened, stacktrace follows");
//            DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "Postman", "updateTable");
            e.printStackTrace();
        }

        if (DEBUG) Log.i(LOG_TAG,"updateTable - Finish update table");
    }

    void closeDatabase(){
        packetsDatabase.close();
    }

}
