package com.example.aalloul.packets;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.aalloul.packets.DataBaseContracts.Postmen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import android.util.Log;

/**
 * Created by aalloul on 03/07/16.
 */
class Postman extends Person {
    // Logging
    private final static String LOG_TAG = "Postman";

    // Some variables
    private Context localContext;
    private Backend backend = new Backend();
    private SQLiteDatabase db;
    private PacketsDatabase packetsDatabase;

    // constructor that returns access to individual postmen
    public Postman (Context ctx, JSONObject userDeclaration) {
        // Calling the super constructor needs to be done first, you dumb ass!
        super(ctx,userDeclaration);
        Log.i(LOG_TAG, "Constructor called");
        localContext = ctx;

    }

    // This constructor allows access to the backend, table update and listView populating
    public Postman (Context ctx, HashMap<String, String> queryParams) {
        super(ctx);
        Log.i(LOG_TAG, "Constructor with query parameters called");

        localContext = ctx;
        //TODO maybe change this to an intent to avoid blocking the thread?
        String t_start = Long.toString(System.currentTimeMillis());
        String t_end   = Long.toString(System.currentTimeMillis()*24*3600*1000);
        queryParams.put("date_pickup", t_start);
        queryParams.put("date_delivery", t_end);
        Log.i(LOG_TAG,"queryParams filled");

        packetsDatabase = new PacketsDatabase(localContext);
        Log.i(LOG_TAG,"Initialize the packetsDatabase");
        db = packetsDatabase.getWritableDatabase();
        Log.i(LOG_TAG,"Get writable packetsDatabase");
        this.updateTable(db,queryParams);

    }


    // Method that returns the whole content of the SQLite table.
    @Override
    protected Cursor getUsers(){

        Log.i(LOG_TAG, " getUsers -- Start getting all postmen from SQLite");
        ArrayList<Postman> myUsers = new ArrayList<Postman>();

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

        Log.i(LOG_TAG, " getUsers -- Return all postmen from SQLite");

        return c;
    }

    protected Cursor get_Data_For_ListView(HashMap<String, String> parameters) {
        // TODO find a way to include neighbouring cities ...
        // TODO if no values returned, widen date range and/or request update of db... tbd outside
        // TODO arrange the ordering of the returned results -- currently ordered by date
        // TODO if backend throws exception, forward it to the main caller.
        // TODO base64 encode/decode of pictures

        Log.i(LOG_TAG, "get_Data_For_ListView -- Start getting all postmen for ListView");
        // Initialize some variables
        Cursor c;
        // this is for the where variable
        String where_col = "";
        List<String> where_val = new ArrayList<String>();

        // This counter allows to know whether an "AND" clause is needed
        int cnt = 0;
        String and;

        // Columns to retrieve
        String[] projection = {
                Postmen.ROW_ID,
                Postmen.COLUMN_NAME_FIRSTNAME,
                Postmen.COLUMN_NAME_NUMBERPACKAGES,
                Postmen.COLUMN_NAME_SOURCECITY,
                Postmen.COLUMN_NAME_SOURCECOUNTRY,
                Postmen.COLUMN_NAME_DESTINATIONCITY,
                Postmen.COLUMN_NAME_DESTINATIONCOUNTRY,
                Postmen.COLUMN_NAME_TAKEBYDATE,
                Postmen.COLUMN_NAME_DELIVERBYDATE,
                Postmen.COLUMN_NAME_POSTMANCOMMENT,
                Postmen.COLUMN_NAME_PHONENUMBER
        };

        // fromCity
        if (parameters.containsKey("source_city")) {
            where_col += Postmen.COLUMN_NAME_SOURCECITY + " = ? ";
            where_val.add(parameters.get("source_city"));
            cnt++;
        }

        // fromCountry
        if (parameters.containsKey("source_country")) {
            and = (cnt > 0) ? " AND ":" ";
            where_col += and + Postmen.COLUMN_NAME_SOURCECOUNTRY + " = ? ";
            where_val.add(parameters.get("source_country"));
            cnt = 1;
        }

        // date start
        if (parameters.containsKey("packet_take_by_date")) {
            and = (cnt > 0) ? " AND ":" ";
            where_col += and + Postmen.COLUMN_NAME_TAKEBYDATE + " > ? ";
            where_val.add(parameters.get("packet_take_by_date"));
            cnt = 1;
        } else {
            Log.i(LOG_TAG, "get_Data_For_ListView - Query params does not contain start time");

            and = (cnt > 0) ? " AND ":" ";
            String t_start = Long.toString(System.currentTimeMillis());
            where_col += and + Postmen.COLUMN_NAME_TAKEBYDATE + " > ? ";
            where_val.add(t_start);
            cnt = 1;
        }

        // Date end
        if (parameters.containsKey("packet_deliver_by_date")) {
            and = (cnt > 0) ? " AND ":" ";
            where_col += and + Postmen.COLUMN_NAME_DELIVERBYDATE + " < ? ";
            where_val.add(parameters.get("packet_deliver_by_date"));
            cnt = 1;
        } else {
            Log.i(LOG_TAG, "get_Data_For_ListView - Query params does not contain end time");
            and = (cnt > 0) ? " AND ":" ";
            String t_end = Long.toString(System.currentTimeMillis() + 24*3600*1000);
            where_col += and + Postmen.COLUMN_NAME_DELIVERBYDATE + " < ? ";
            where_val.add(t_end);
            cnt = 1;
        }

        // Size package
        if (parameters.containsKey("packet_size")) {
            and = (cnt > 0) ? " AND ":" ";
            where_col += and + Postmen.COLUMN_NAME_SIZEPACKAGES + " = ? ";
            where_val.add(parameters.get("packet_size"));
            cnt = 1;
        }

        // Number of packages
        if (parameters.containsKey("number_packages")) {
            and = (cnt > 0) ? " AND ":" ";
            where_col += and + Postmen.COLUMN_NAME_NUMBERPACKAGES + " = ? ";
            where_val.add(parameters.get("number_packages"));
            cnt = 1;
        }

        // Sort order
        String sortOrder = Postmen.COLUMN_NAME_TAKEBYDATE + " ASC";

        // Change where_val to an array
        String[] where_val_array = new String[ where_val.size() ];
        where_val.toArray(where_val_array);

        // Some Debug
        Log.i(LOG_TAG, "get_Data_For_ListView - projection is "+ Arrays.toString(projection));
        Log.i(LOG_TAG, "get_Data_For_ListView - where col is "+where_col);
        Log.i(LOG_TAG, "get_Data_For_ListView - where val is "+Arrays.toString(where_val_array));
        Log.i(LOG_TAG, "get_Data_For_ListView - sortOder = "+sortOrder);


        c = db.query(Postmen.TABLE_NAME, new String[]{"*"} ,null, null, null, null, null);

        Log.i(LOG_TAG,"get_Data_For_ListView - The database contains " + c.getCount()+" entries, in total.");

        // The query itself
        c = db.query(
                Postmen.TABLE_NAME,  // The table to query
                projection,          // The columns to return
                where_col,           // The columns for the WHERE clause
                where_val_array,     // The values for the WHERE clause
                null,                // don't group the rows
                null,                // don't filter by row groups
                sortOrder            // The sort order
        );

        Log.i(LOG_TAG, "get_Data_For_ListView - The actual query returned "+c.getCount()+" entries");
        Log.i(LOG_TAG, "get_Data_For_ListView -- End");
        return c;

    }


    protected void updateTable (SQLiteDatabase db, HashMap<String, String> queryParameters) {
        /* TODO the back-end result will contain all the neighboring cities in an array, something
        like [ {...., "cities": [city_1, city_2], "distances":[dist_1, dist_2]},... ]
         up to this method to flatten this
         */
        /* TODO Make sure this is not called whenever the main View is called */
        Log.i(LOG_TAG,"updateTable - Start");

        Log.i(LOG_TAG,"updateTable - Request back-end");
        String resu = backend.requestUpdate(queryParameters);

        Log.d(LOG_TAG, "updateTable - resu = "+resu);

        // Initialize variable
        Log.i(LOG_TAG,"updateTable - Initialize all variables");
        JSONArray all_updates;


        try {
            all_updates = new JSONArray(resu);
            Log.i(LOG_TAG, "updateTable - After parsing to JSON, we have "+
                    all_updates.length() +" new entries");

            for (int i = 0; i < all_updates.length(); i++){
                JSONObject updated_user = all_updates.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put(Postmen.COLUMN_NAME_NAME , updated_user.getString( "name"));
                values.put(Postmen.COLUMN_NAME_FIRSTNAME , updated_user.getString( "firstname"));
                values.put(Postmen.COLUMN_NAME_CITY , updated_user.getString( "city"));
                values.put(Postmen.COLUMN_NAME_STREET , updated_user.getString( "street"));
                values.put(Postmen.COLUMN_NAME_COUNTRY , updated_user.getString( "country"));
                values.put(Postmen.COLUMN_NAME_PHONENUMBER ,
                        updated_user.getString( "phone_number"));
                values.put(Postmen.COLUMN_NAME_RATING , updated_user.getInt( "user_rating"));
                values.put(Postmen.COLUMN_NAME_SOURCECITY ,
                        updated_user.getString("source_city"));
                values.put(Postmen.COLUMN_NAME_SOURCECOUNTRY ,
                        updated_user.getString("source_country"));
                values.put(Postmen.COLUMN_NAME_SOURCESTREET ,
                        updated_user.getString("source_street"));
                values.put(Postmen.COLUMN_NAME_DESTINATIONCITY ,
                        updated_user.getString("destination_city"));
                values.put(Postmen.COLUMN_NAME_DESTINATIONCOUNTRY ,
                        updated_user.getString("destination_country"));
                values.put(Postmen.COLUMN_NAME_DESTINATIONSTREET ,
                        updated_user.getString("destination_street"));
                values.put(Postmen.COLUMN_NAME_POSTMANCOMMENT ,
                        updated_user.getString("transport_comment"));
                values.put(Postmen.COLUMN_NAME_SIZEPACKAGES , updated_user.getString("packet_size"));
                values.put(Postmen.COLUMN_NAME_NUMBERPACKAGES ,
                        updated_user.getInt("number_packages"));
                values.put(Postmen.COLUMN_NAME_TAKEBYDATE ,
                        updated_user.getString("packet_take_by_date"));
                values.put(Postmen.COLUMN_NAME_DELIVERBYDATE ,
                        updated_user.getString("packet_deliver_by_date"));
                values.put(Postmen.COLUMN_NAME_TRANSPORTMETHOD ,
                        updated_user.getString("package_transport_method"));

//                db.insert(
//                        Postmen.TABLE_NAME,
//                        "",
//                        values);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "updateTable - An error with JSON happened, stacktrace follows");
            e.printStackTrace();
        } catch (Exception e){
            Log.e(LOG_TAG, "updateTable - An unknown error happened, stacktrace follows");
            e.printStackTrace();
        }

        Log.i(LOG_TAG,"updateTable - Finish update table");
    }

    public void closeDatabase(){
        db.close();
    }

}
