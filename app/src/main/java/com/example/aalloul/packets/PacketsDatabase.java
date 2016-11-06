package com.example.aalloul.packets;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Import our contract classes
import com.example.aalloul.packets.DataBaseContracts.Senders;
import com.example.aalloul.packets.DataBaseContracts.Postmen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

/**
 * Created by aalloul on 10/07/16.
 * manages lifecycle of the tables and databases.
 * it also calls
 */
class PacketsDatabase extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 6; // 06-Nov-2016 15:25
    public static final String DATABASE_NAME = "Persons.db";
    private Context localContext;
    private static final String LOG_TAG = "PacketsDatabase";
    private Backend backend;


    public PacketsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        localContext = context;
        backend = new Backend();
    }

    // Table creation
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG,"OnCreate - Start");
        Log.i(LOG_TAG, "OnCreate - Senders table "+ Senders.CREATE_TABLE);
        db.execSQL(Senders.CREATE_TABLE);
        Log.i(LOG_TAG, "OnCreate - Postmen table "+ Postmen.CREATE_TABLE);
        db.execSQL(Postmen.CREATE_TABLE);
        Log.i(LOG_TAG,"OnCreate - End");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database version has changed. If you need your customers
        // to keep their old data, you need to put that logic here... for example, read information
        // from old tables and insert into new tables. This upgrade happens only once when the app is
        // updated
        db.execSQL(Senders.DELETE_TABLE);
        db.execSQL(Postmen.DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}