package com.example.aalloul.packets;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aalloul on 03/07/16.
 */
class User extends Person{
    //TODO complete this

    private Context localContext;
    private final static String LOG_TAG = "Receiver";

    // For receivers, constructor requires all information
    public User (Context ctx, JSONObject userDeclaration) {
        // Calling the super constructor needs to be done first, you dumb ass!
        super(ctx,userDeclaration);

        Log.i(LOG_TAG, "Constructor called");
        localContext = ctx;

    }

    public Cursor getUsers () {
        ArrayList<Person> myUsers = new ArrayList<Person>();

        // Query the SQLite db
        PacketsDatabase packetsDatabase = new PacketsDatabase(localContext);
        SQLiteDatabase db = packetsDatabase.getReadableDatabase();


        Cursor c = db.query(
                DataBaseContracts.Postmen.TABLE_NAME,  // The table to query
                null,                // The columns to return
                null,                // The columns for the WHERE clause
                null,                // The values for the WHERE clause
                null,                // don't group the rows
                null,                // don't filter by row groups
                null                 // The sort order
        );

        return c;
    }

    protected void postUpdate(){

    };
}
