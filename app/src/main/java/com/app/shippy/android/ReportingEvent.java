package com.app.shippy.android;
import android.util.Log;
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
    private String version = "0.1";
    private final String LOG_TAG = "ReportingEvent";
    private final boolean DEBUG = true;

    ReportingEvent() {
        sessionStart = Utilities.CurrentTimeMS();
        fragmentStart = sessionStart;
        events = new ArrayList<>();
        fragmentName = "None";
    }

    void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    void setFragmentStart(long time) {
        this.fragmentStart = time;
    }

    void setFragmentEnd() {fragmentEnd = Utilities.CurrentTimeMS();}
    void setSessionEnd() {sessionEnd= Utilities.CurrentTimeMS();}


    @SuppressWarnings("unchecked")
    private HashMap enrichEvent(HashMap tmp) {
        tmp.put("sessionStart", sessionStart);
        tmp.put("activityName", activityName);
        tmp.put("fragmentName", fragmentName);
        tmp.put("dataModelVersion", version);
        tmp.put("fragmentStart", fragmentStart);

        if (sessionEnd > 0) {
            tmp.put("sessionEnd", sessionEnd);
            tmp.put("sessionDuration", sessionEnd - sessionStart);
        }

        if (fragmentEnd > 0) {
            tmp.put("fragmentEnd",fragmentEnd);
            tmp.put("fragmentDuration",fragmentEnd - fragmentStart);
        }

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

        if ( (args.length & 1) != 0 ) { throw new IllegalArgumentException();}

        for (Object arg : args) {
            ++i;
            if (i == 1) key = (String) arg;
            if (i == 2) {
                tmp.put(key, arg);
                i = 0;
            }
        }
        events.add(enrichEvent(tmp));
        Gson gson = new Gson();

        if (DEBUG) Log.i(LOG_TAG, "events = "+ gson.toJson(events));
    }

    @SuppressWarnings("unchecked")
    void addException(String cause, String tostring, String action) {
        HashMap tmp = new HashMap<>();
        tmp.put("Exception",true);
        tmp.put("ExceptionCausedBy", cause);
        tmp.put("ExceptionToString", tostring);
        tmp.put("ExceptionAction", action);
        events.add(enrichEvent(tmp));
        if (DEBUG) Log.i(LOG_TAG, "exception = "+ events);

    }

    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String, Object>> getEvents() {
        return events;
    }

    void clearEvents() {
        events.clear();
    }
}
