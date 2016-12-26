package com.example.aalloul.packets;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by adamalloul on 03/12/2016.
 */

class DataBuffer {
    // We want this class to be somehow static
    private DataBuffer(){}

    // for the logger
    private static final String LOG_TAG = "DataBuffer";

    // This is an Arraylist of hashmaps that will be dumped into a JSON string -- Serves as a buffer
    private static ArrayList<String> loggerData = new ArrayList<>();

    // This is some kind of timer that tells whether we should or not submit the data to the backend
    private static long first_update = 0;

    // The usage data is appended here -- requires a lock to avoid both the backend and logger
    // accessing at the same time this list
    static void addEvent(String data) {
        Log.i(LOG_TAG, "updateLoggerData - Enter");

        // Require list and append the data to loggerData
        final ReentrantLock loggerlock = new ReentrantLock();
        loggerlock.lock();
        try {
            loggerData.add(data);
        } finally {
            loggerlock.unlock();
        }

        // Update the timer if its value is not initialized yet
        if (first_update == 0) {
            first_update = Utilities.CurrentTimeMS();
        }

        Log.i(LOG_TAG, "updateLoggerData - Exit");
    }


    // Called by the Backend intent service to retrieve the data and post it to the backend
    // requests a lock on loggerData in order to get its content and clear it
    @Nullable
    static String getTheData() {
        Log.i(LOG_TAG, "getTheData - Enter");

        if (loggerData.isEmpty()) {
            return null;
        }

        Gson gson = new Gson();
        final ReentrantLock posterlock = new ReentrantLock();
        String jsonData;
        Log.i(LOG_TAG, "acquiring lock");

        posterlock.lock();
        try {
            jsonData = gson.toJson(loggerData);
            loggerData.clear();
        } finally {
            posterlock.unlock();
            Log.i(LOG_TAG, "getTheData - Lock released");
        }

        // Update the timer
        Log.i(LOG_TAG, "getTheData - Update the timer");
        first_update = Utilities.CurrentTimeMS();

        Log.i(LOG_TAG, "getTheData - Exit");
        return jsonData;
    }

    // Accessed by the Backend service to reset the timer
    private void resetTheTime() {
        Log.i(LOG_TAG, "resetTheTime - Enter");
        first_update = Utilities.CurrentTimeMS();
        Log.i(LOG_TAG, "resetTheTime - Exit");
    }

    // Method to determine whether we should post the usage data to the backend
    static boolean ShouldIPostData() {
        Log.i(LOG_TAG, "ShouldIPostData - Enter");

        // Not sure whether needed
        if (first_update == 0) {
            Log.i(LOG_TAG, "ShouldIPostData - Called before the buffer was initialized");
            return false;
        }

        if (Utilities.CurrentTimeMS() - first_update > R.integer.time_to_post_usage_data - 2000 ) {
            Log.i(LOG_TAG, "ShouldIPostData - yes!");
            return true;
        } else {
            Log.i(LOG_TAG, "ShouldIPostData - not yet!");
            return false;
        }
    }
}
