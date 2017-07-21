package com.app.shippy.android;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adamalloul on 28/06/2017.
 */

class ReportingEvent {
    private long sessionStart, sessionEnd, fragmentStart, fragmentEnd;
    private String activityName;
    private ArrayList<HashMap<String, Object> > events;
    private String fragmentName;
    private double version = 0.1;
    private final String LOG_TAG = "ReportingEvent";
    private final boolean DEBUG = false;
    private boolean END_SESSION = true;
    private String deviceId, deviceType;
    private ArrayList<String> ids = new ArrayList<>();
    private static ReportingEvent mInstance = new ReportingEvent();

    ReportingEvent() {
        sessionStart = Utilities.CurrentTimeMS();
        fragmentStart = sessionStart;
        events = new ArrayList<>();
        fragmentName = "None";
    }

    static synchronized ReportingEvent getInstance() {
        // We want this class to be a singleton
        if (mInstance == null) {mInstance = new ReportingEvent();}
        return mInstance;
    }

    void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    String getDeviceId() {
        return deviceId;
    }

    void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    String getDeviceType() {
        return deviceType;
    }

    void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    void setFragmentStart(long time) {
        this.fragmentStart = time;

        // It doesn't make sense to have a fragmentStart whose value is larger
        // than fragmentEnd
        if (this.fragmentEnd < this.fragmentStart) {
            fragmentEnd = 0;
        }
    }

    void setFragmentEnd() {fragmentEnd = Utilities.CurrentTimeMS();}

    void setSessionEnd() {sessionEnd= Utilities.CurrentTimeMS();}

    void setend_session(boolean val) {
        END_SESSION = val;
    }

    boolean end_session() {
        return END_SESSION;
    }

    private boolean isDuplicate(String stringrep) {
        for (String id : ids) {
            if (id.equals(stringrep)) {
                return true;
            }
        }

        ids.add(stringrep);

        return false;
    }

    @SuppressWarnings("unchecked")
    private HashMap enrichEvent(HashMap tmp) {
        tmp.put("sessionStart", sessionStart);
        tmp.put("activityName", activityName);
        tmp.put("fragmentName", fragmentName);
        tmp.put("dataModelVersion", version);
        tmp.put("fragmentStart", fragmentStart);
        // Add a timestamp for all events
        tmp.put("timestamp", Utilities.CurrentTimeMS());

        if (sessionEnd > 0) {
            tmp.put("sessionEnd", sessionEnd);
            tmp.put("sessionDuration", sessionEnd - sessionStart);
        }

        if (fragmentEnd > 0) {
            tmp.put("fragmentEnd",fragmentEnd);
            tmp.put("fragmentDuration",fragmentEnd - fragmentStart);
        }

        String jsonrep = tmp.toString();

        // Check For duplicate
        if (isDuplicate(jsonrep)) {return null;}
        return tmp;
    }

    /**
     * This method requires an even number of arguments to work. These are then
     * treated as key, value pairs where the key is a String and the value a
     * generic Object
     * @param args: Any even number of arguments that can be treated as a key, value pair. The key
     *            must be a String.
     * @throws IllegalArgumentException if number of arguments is not even
     */
    @SuppressWarnings("unchecked")
    void addEvent(Object... args) throws IllegalArgumentException{
        HashMap tmp = new HashMap<>();
        String key = "";
        int i = 0;

        // Check whether the number of arguments is even
        if ( (args.length & 1) != 0 ) { throw new IllegalArgumentException();}

        for (Object arg : args) {
            ++i;
            if (i == 1) key = (String) arg;
            if (i == 2) {
                tmp.put(key, arg);
                i = 0;
            }
        }

        UpdateEvents(enrichEvent(tmp));
        Gson gson = new Gson();

        if (DEBUG) Log.i(LOG_TAG, "events = "+ gson.toJson(events));
    }

    private void UpdateEvents(HashMap event) {
        if (event == null) return;

        events.add(event);
    }

    @SuppressWarnings("unchecked")
    void addException(String cause, String tostring, String action) {
        HashMap tmp = new HashMap<>();
        tmp.put("Exception",true);
        tmp.put("ExceptionCausedBy", cause);
        tmp.put("ExceptionToString", tostring);
        tmp.put("ExceptionAction", action);
        events.add(enrichEvent(tmp));
        Gson gson = new Gson();
        if (DEBUG) Log.i(LOG_TAG, "exception = "+ gson.toJson(events));

    }

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String, Object>> getEvents() {
        Gson gson = new Gson();
        Log.i(LOG_TAG, "getEvents - " + gson.toJson(events));
        return events;
    }

    int getNumberEvents() {
        return events.size();
    }

    void clearEvents() {
        Log.i(LOG_TAG, "clearEvnets - Clearing events" );
        events.clear();
        ids.clear();
        Log.i(LOG_TAG, "clearEvnets - Events left = " + events.size() );
    }
}
