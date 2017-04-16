package com.example.aalloul.packets;

import android.support.annotation.Nullable;
import android.util.Log;
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
    private static String deviceId, deviceType;
    private final static boolean DEBUG = true;

    // This is an Arraylist of hashmaps that will be dumped into a JSON string -- Serves as a buffer
    private static ArrayList<HashMap<String, String>> loggerData = new ArrayList<>();

    // This is some kind of timer that tells whether we should or not submit the data to the backend
    private static long first_update = 0;

    // The usage data is appended here -- requires a lock to avoid both the backend and logger
    // accessing at the same time this list
    static void addEvent(HashMap<String, String> data) {
        if (DEBUG) Log.i(LOG_TAG, "updateLoggerData - Enter");

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

        if (DEBUG) Log.i(LOG_TAG, "updateLoggerData - loggerData "+loggerData);
        if (DEBUG) Log.i(LOG_TAG, "updateLoggerData - Exit");
    }

    // Generic method to catch exceptions
    static void addException(String stack, String toString, String className, String method ){
        HashMap<String, String> st = new HashMap<>();
        st.put("time", Long.toString(Utilities.CurrentTimeMS()));
        st.put("class_name",className);
        st.put("method_name",method);
        st.put("stack_trace",stack);
        st.put("exception_to_string",toString);
        addEvent(st);
    }

    static void clear(){
        loggerData.clear();
    }

    // Called by the Backend intent service to retrieve the data and post it to the backend
    // requests a lock on loggerData in order to get its content and clear it
    @Nullable
    static ArrayList<HashMap<String,String>> getTheData() {
        if (DEBUG) Log.i(LOG_TAG, "getTheData - Enter");

        if (DEBUG) Log.i(LOG_TAG, "getTheData - loggerData = " + loggerData);
        if (loggerData.isEmpty()) {
            return null;
        }
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        final ReentrantLock posterlock = new ReentrantLock();

        if (DEBUG) Log.i(LOG_TAG, "acquiring lock");

        posterlock.lock();
        try {
            jsonData.addAll(loggerData);
            loggerData.clear();
        } finally {
            posterlock.unlock();
            if (DEBUG) Log.i(LOG_TAG, "getTheData - Lock released");
        }

        // Update the timer
        if (DEBUG) Log.i(LOG_TAG, "getTheData - Update the timer");
        resetTheTime();

        if (DEBUG) Log.i(LOG_TAG, "getTheData - jsonData = " + jsonData);
        return jsonData;
    }

    private static void resetTheTime() {
        if (DEBUG) Log.i(LOG_TAG, "resetTheTime - Enter");
        first_update = Utilities.CurrentTimeMS();
        if (DEBUG) Log.i(LOG_TAG, "resetTheTime - Exit");
    }

    // Method to determine whether we should post the usage data to the backend
    static boolean ShouldIPostData() {
        if (DEBUG) Log.i(LOG_TAG, "ShouldIPostData - Enter");

        // Not sure whether needed
        if (first_update == 0) {
            if (DEBUG) Log.i(LOG_TAG, "ShouldIPostData - Called before the buffer was initialized");
            return false;
        }

        if ( (Utilities.CurrentTimeMS() - first_update) >= (R.integer.time_to_post_usage_data) ) {
            if (DEBUG) Log.i(LOG_TAG, "ShouldIPostData - yes!");
            return true;
        } else {
            if (DEBUG) Log.i(LOG_TAG, "ShouldIPostData - not yet!");
            return false;
        }
    }

    // device ID and type
    static void setDeviceId(String devid) {
        deviceId = devid;
    }

    // get DeviceID
    static String getDeviceId() {
        return deviceId;
    }

    static void setDeviceType(String devtype) {
        deviceType = devtype;
    }
    static String getDeviceType() {
        return "";
    }
}
