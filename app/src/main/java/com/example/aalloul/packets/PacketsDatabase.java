package com.example.aalloul.packets;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.aalloul.packets.DataBaseContracts.Postmen;

/**
 * Created by aalloul on 10/07/16.
 * manages lifecycle of the tables and databases.
 * it also calls
 */
class PacketsDatabase extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 8; // 16-Apr-2016 10:37
    private static final String DATABASE_NAME = "Persons.db";
    private static final String LOG_TAG = "PacketsDatabase";
    private final static boolean DEBUG = false;

    PacketsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Table creation
    public void onCreate(SQLiteDatabase db) {
        if (DEBUG) Log.i(LOG_TAG, "OnCreate - Postmen table "+ Postmen.CREATE_TABLE);
        db.execSQL(Postmen.CREATE_TABLE);
        if (DEBUG) Log.i(LOG_TAG,"OnCreate - End");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when the database version has changed. If you need your customers
        // to keep their old data, you need to put that logic here... for example, read information
        // from old tables and insert into new tables. This upgrade happens only once when the app is
        // updated
        db.execSQL(Postmen.DELETE_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}