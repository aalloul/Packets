package com.example.aalloul.packets;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;


public class BackendInteraction extends IntentService {

    // Logger for this class
    // create a local variable for identifying the class where the log statements come from
    private final static String LOG_TAG = BackendInteraction.class.getSimpleName();

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_QUERY_DB = "com.example.aalloul.packets.action.QueryDB";
    private static final String ACTION_REPORT_LOGGER = "com.example.aalloul.packets.action.ReportLogger";

    /* *********************
    URL parameters
     ********************** */
    private HttpURLConnection conn;
    DataOutputStream encoded_payload;

    /* **********************************
    Params for the request to the backend
    ********************************** */
    // time params
    private static final String TS_FROM = "com.example.aalloul.packets.extra.ts_from";
    private static final String TS_TO = "com.example.aalloul.packets.extra.ts_to";
    // geo params
    private static final String COUNTRY_FROM = "com.example.aalloul.packets.extra.country_from";
    private static final String COUNTRY_TO = "com.example.aalloul.packets.extra.country_to";
    private static final String CITY_FROM = "com.example.aalloul.packets.extra.city_from";
    private static final String CITY_TO = "com.example.aalloul.packets.extra.city_to";
    // package params
    private static final String PACKAGE_SIZE = "com.example.aalloul.packets.extra.package_size";
    private static final String PACKAGE_NUMBER = "com.example.aalloul.packets.extra.package_number";

    // the intent's intent (get or post)
    private static final String INTENT_INTENT = "com.example.aalloul.packets.extra.intent_intent";

    /* **********************************
    Params for the usage logging data
    ********************************** */
    // time params
    private static final String USAGE_DATA = "com.example.aalloul.packets.extra.usage_data";

    public BackendInteraction() {
        super("BackendInteraction");
        Log.w(LOG_TAG, "BackendInteraction - New instance");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     * @param action This parameter is either "POST" (i.e. posting a new offer) or "GET" (i.e.
     *               updating the list of offers)
     */
    public static void startActionQueryDB(Context context, HashMap<String, String> paramBlob,
                                          String action) {
        Log.w(LOG_TAG, "startActionQueryDB - Enter");
        Intent intent = new Intent(context, BackendInteraction.class);
        intent.setAction(ACTION_QUERY_DB);

        // Are we trying to post a new offer or to retrieve new offers
        Log.w(LOG_TAG, "startActionQueryDB - action is " + action);
        intent.putExtra(INTENT_INTENT, action);

        // Time parameters
        if (paramBlob.containsKey("TS_FROM")) {
            intent.putExtra(TS_FROM, paramBlob.get("TS_FROM"));
        } else {
            intent.putExtra(TS_FROM, Long.toString(Utilities.CurrentTimeMS()));
        }
        if (paramBlob.containsKey("TS_TO")) {
            intent.putExtra(TS_TO, paramBlob.get("TS_TO"));
        } else {
            intent.putExtra(TS_TO, Long.toString(Utilities.CurrentTimeMS()));
        }

        // Geo parameters
        if (paramBlob.containsKey("COUNTRY_FROM")) {
            intent.putExtra(COUNTRY_FROM,paramBlob.get("COUNTRY_FROM"));
        } else {
            intent.putExtra(COUNTRY_FROM,"unkonwn");
        }
        if (paramBlob.containsKey("COUNTRY_TO")) {
            intent.putExtra(COUNTRY_TO,paramBlob.get("COUNTRY_TO"));
        } else {
            intent.putExtra(COUNTRY_TO,"unkonwn");
        }
        if (paramBlob.containsKey("CITY_TO")) {
            intent.putExtra(CITY_TO,paramBlob.get("CITY_TO"));
        } else {
            intent.putExtra(CITY_TO,"unkonwn");
        }
        if (paramBlob.containsKey("CITY_FROM")) {
            intent.putExtra(CITY_FROM,paramBlob.get("CITY_FROM"));
        } else {
            intent.putExtra(CITY_FROM,"unkonwn");
        }

        // Package parameters
        if (paramBlob.containsKey("PACKAGE_NUMBER")) {
            intent.putExtra(PACKAGE_NUMBER,paramBlob.get("PACKAGE_NUMBER"));
        } else {
            intent.putExtra(PACKAGE_NUMBER,"unkonwn");
        }
        if (paramBlob.containsKey("PACKAGE_SIZE")) {
            intent.putExtra(PACKAGE_SIZE,paramBlob.get("PACKAGE_SIZE"));
        } else {
            intent.putExtra(PACKAGE_SIZE,"unkonwn");
        }

        // Start the service
        context.startService(intent);
        Log.w(LOG_TAG, "startActionQueryDB - Exit");
    }


    /**
     * Starts this service to perform action startActionReportLogger. If
     * the service is already performing a task this action will be queued.
     * Usage:
     * @param context is mandatory
     * @param force provides the possibility to force the data post, even if the threshold delay
     *              is not reached. Should be set to true only when quitting the application.
     * @see IntentService
     */
    public static void startActionReportLogger(Context context, boolean force) {
        Log.w(LOG_TAG, "startActionReportLogger - Enter");

        if (DataBuffer.getTheData() == null) {
            Log.w(LOG_TAG, "startActionReportLogger - no data to post ");
            return;
        }

        if (DataBuffer.ShouldIPostData() || force) {
            Intent intent = new Intent(context, BackendInteraction.class);
            intent.setAction(ACTION_REPORT_LOGGER);
            intent.putExtra(USAGE_DATA, DataBuffer.getTheData());
            context.startService(intent);
        }
        Log.w(LOG_TAG, "startActionReportLogger - Exit");
    }

    // The default value for force is False
    public static void startActionReportLogger(Context context) {
        startActionReportLogger(context, false);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.w(LOG_TAG, "onHandleIntent - Enter");
        if (intent != null) {
            Log.w(LOG_TAG, "onHandleIntent - Intent not null");
            final String action = intent.getAction();
            Log.w(LOG_TAG, "onHandleIntent - Action is ="+ action);
            if (ACTION_QUERY_DB.equals(action)) {
                // Time params
                final String ts_from = intent.getStringExtra(TS_FROM);
                final String ts_to = intent.getStringExtra(TS_TO);

                // Geo params
                final String country_from = intent.getStringExtra(COUNTRY_FROM);
                final String country_to = intent.getStringExtra(COUNTRY_TO);
                final String city_from = intent.getStringExtra(CITY_FROM);
                final String city_to = intent.getStringExtra(CITY_TO);

                // Package size
                final String package_size = intent.getStringExtra(PACKAGE_SIZE);
                final String package_number = intent.getStringExtra(PACKAGE_NUMBER);

                /* Intent's intent
                This parameter is used to know whether we're posting or retrieving offers
                 */

                final String intent_intent = intent.getStringExtra(INTENT_INTENT);

                // handle the request
                handleActionQueryDB(ts_from, ts_to, country_from, country_to, city_from, city_to,
                package_size, package_number, intent_intent);

            } else if (ACTION_REPORT_LOGGER.equals(action)) {
                final String usage_data = intent.getStringExtra(USAGE_DATA);
                handleActionReportLogger(usage_data);

            } else {
                Log.e(LOG_TAG, "onHandleIntent - Unknown action!");
            }
        } else {
            Log.w(LOG_TAG, "onHandleIntent - Intent is null");
        }
        Log.w(LOG_TAG, "onHandleIntent - Exit");
    }


    /** Queries the back-end to post/retrieve new offers and triggers a post of usage data
     * Does not perform checkNetwork() as this is done by calling method. This is to inform the
     * user that no network is available if that is the case.
     * @param action is used to know whether we're posting a new offer or requesting an update
     */
    private void handleActionQueryDB(String ts_from, String ts_to, String country_from,
                                     String country_to, String city_from, String city_to,
                                     String package_size, String package_number, String action) {
        Log.w(LOG_TAG, "handleActionQueryDB - Enter");

        // First create a HashMap with all parameters
        HashMap<String, String> payload= new HashMap<>();
        payload.put("ts_from",ts_from);
        payload.put("ts_to",ts_to);
        payload.put("country_to",country_to);
        payload.put("country_from",country_from);
        payload.put("city_to",  city_to);
        payload.put("city_from",city_from);
        payload.put("package_size",  package_size);
        payload.put("package_number",package_number);

        // then transform the payload to a json string and perform network call
        if (action.equals("GET")) {
            Log.w(LOG_TAG, "handleActionQueryDB - Requesting update from backend");
            doTheNetworkOperation(action, new Gson().toJson(payload));
        } else {
            Log.w(LOG_TAG, "handleActionQueryDB - Posting new offer to backend");
            doTheNetworkOperation(action, new Gson().toJson(payload), true);
        }

        // TODO Update the SQLite database
        if (action.equals("GET")) {
            // Insert the dat to the DB
            insertDataToDB();
            // Notify the UI
            Intent intent = new Intent("newDataHasArrived");
            intent.putExtra("queryResult", "ok");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        Log.w(LOG_TAG, "handleActionQueryDB - Using the query excuse to post usage data");
        handleActionReportLogger(DataBuffer.getTheData(), false);

        closeConnection();
        Log.w(LOG_TAG, "handleActionQueryDB - Exit");
    }

    // Posts the usage data to the backend -- called when the timer to post data should be ignored
    private void handleActionReportLogger(String usage_data) {
        // This method is used by other classes/activities
        // The network check is required but outside the main thread to avoid blocking UI
        // If no network, we should just postpone the post
        handleActionReportLogger(usage_data, true);

    }

    /** This method is intended to send the usage data to the backend, it is thus not intended to be
     * as lightweight as possible wrt to the main thread, i.e. the
     * @param shouldICheckNetowrk should be set to true so the check is done during the intent
     */
    private void handleActionReportLogger(String usage_data, boolean shouldICheckNetowrk) {
        Log.w(LOG_TAG, "handleActionReportLogger - Enter");

        // Check network conditions and perform action
        if (shouldICheckNetowrk) {
            if (checkNetwork()) {
                Log.w(LOG_TAG, "handleActionReportLogger - Network ok -- Send the data");
                doTheNetworkOperation("POST", usage_data);
                closeConnection();
                return;
            } else {
                Log.w(LOG_TAG, "handleActionReportLogger - Network NOK -- buffer data");
            // TODO find a way to have a back-off procedure
                return;
        } }

        doTheNetworkOperation("POST", usage_data);


        Log.w(LOG_TAG, "handleActionReportLogger - Exit");
    }

    // Check network status
    private boolean checkNetwork() {
        // Get the connection manager
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Check whether conditions are okay
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.w(LOG_TAG, "isNetworkOk - Network available");
            return true;
        } else {
            Log.w(LOG_TAG, "isNetworkOk - Network not available");
            return false;
        }
    }

    // Update the SQLite database
    private void insertDataToDB(){
        Log.w(LOG_TAG, "insertDataToDB - Enter");
        Log.w(LOG_TAG, "insertDataToDB - Exit");

    }

    // Open connection
    private void openConnection(){
        Log.w(LOG_TAG,"openConnection - Enter");
        URL url;
        String raw_url = getResources().getString(R.string.backend_url);
        try {
            Log.w(LOG_TAG, "openConnection - enter the try");
            url = new URL(raw_url);
            Log.w(LOG_TAG, "openConnection - url initialized");
            conn = (HttpURLConnection) url.openConnection();
            Log.w(LOG_TAG, "openConnection - openconnection done");
            conn.setReadTimeout(getResources().getInteger(R.integer.connection_read_time_out));
            Log.w(LOG_TAG, "openConnection - setReadTimeout done");
            conn.setConnectTimeout(getResources().getInteger(R.integer.connection_time_out));
            Log.w(LOG_TAG, "openConnection - setConnectTimeout done");
            conn.setDoInput(true);
            Log.w(LOG_TAG, "openConnection - setDoInput done");
            conn.setDoOutput(true);
            Log.w(LOG_TAG, "openConnection - setDoOutput done");
            conn.setRequestProperty("Content-Type","application/json");
            Log.w(LOG_TAG, "openConnection - setRequestProperty done");

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"openConnection - MalformedURLException");
            DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "BackendInteraction",
                    "openConnection");

            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG,"openConnection - IOException");
            DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "BackendInteraction",
                    "openConnection");

            e.printStackTrace();
        }
        Log.w(LOG_TAG,"openConnection - Exit");
    }

    // Called when not posting a new offer to the backend
    private String doTheNetworkOperation (String operation, String payload) {
        return doTheNetworkOperation (operation, payload, false);
    }

    // Do the network operation here
    @Nullable
    private String doTheNetworkOperation (String operation, String payload, boolean postoffer)  {
        InputStream is;
        String contentAsString = null;

        // if connection not open yet, open it
        if (conn == null) {
            Log.w(LOG_TAG, "doTheNetworkOperation - no connection found");
            openConnection();
        }

        // check whether connection has timedout
        if (conn.getExpiration() > Utilities.CurrentTimeMS() ) {
            Log.w(LOG_TAG, "doTheNetworkOperation - connection expired");
            openConnection();
        }

        // Post or GET is performed here
        try {
            Log.w(LOG_TAG, "doTheNetworkOperation - enter try-catch block");
            conn.setRequestMethod(operation);
            Log.w(LOG_TAG, "doTheNetworkOperation - setRequestMethod");
            conn.connect();
            // TODO add the payload the data
//            writeToOutputStream(payload);
//            Log.w(LOG_TAG, "doTheNetworkOperation - writeToOutputStream done");

            Log.w(LOG_TAG, "doTheNetworkOperation - connect done");

            // Handle response codes
            int response = conn.getResponseCode();
            if (response > 200 && response < 300 ) {
                Log.w(LOG_TAG, "doTheNetworkOperation - The response is: " + response);
                is = conn.getInputStream();
                // TODO check whether this is needed for all operations
                contentAsString = readIt(is, 30000);
                Log.w(LOG_TAG, "contenstAsString = "+contentAsString);

            } else {
                Log.w(LOG_TAG, "doTheNetworkOperation - response status is "+response);
                if (operation.equals("POST") && !postoffer) {
                    handleNetworkErrors("POST");
                } else if (operation.equals("GET")) {
                    handleNetworkErrors("GET");
                } else if (postoffer) {
                    handleNetworkErrors("NewOffer");
                }
            }
        } catch (ProtocolException e) {
            Log.e(LOG_TAG, "doTheNetworkOperation - Protocol exception found");
            DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "BackendInteraction",
                    "doTheNetworkOperation");

            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "doTheNetworkOperation - IO exception found");
            DataBuffer.addException(Arrays.toString(e.getStackTrace()), e.toString(), "BackendInteraction",
                    "doTheNetworkOperation");

            e.printStackTrace();
        } finally {
            return contentAsString;
        }
    }

    // Read the input stream and output the encoded string
    private String readIt(InputStream stream, int len) throws IOException {
        Reader reader;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    // Close the connection when not needed
    private void closeConnection(){
        conn.disconnect();
    }

    // Write data to output stream
    private void writeToOutputStream(String outString) throws IOException {
        // Add the payload
        Log.w(LOG_TAG, "writeToOutputStream - Start");
        byte[] outputInBytes = outString.getBytes("UTF-8");
        Log.w(LOG_TAG, "writeToOutputStream - getBytes done");
        OutputStream os = conn.getOutputStream();
        Log.w(LOG_TAG, "writeToOutputStream - getOutputStream done");
        os.write( outputInBytes );
        Log.w(LOG_TAG, "writeToOutputStream - write done");
        os.close();
        Log.w(LOG_TAG, "writeToOutputStream - close done");

//        encoded_payload = new DataOutputStream(conn.getOutputStream ());
//        Log.w(LOG_TAG, "writeToOutputStream - encoded_payload done");
//        encoded_payload.writeBytes(URLEncoder.encode(outString,"UTF-8"));
//        Log.w(LOG_TAG, "writeToOutputStream - writeBytes done");
//        encoded_payload.flush ();
//        Log.w(LOG_TAG, "writeToOutputStream - flush done");
//        encoded_payload.close ();
//        Log.w(LOG_TAG, "writeToOutputStream - close done");
    }

    // Handle network errors
    private void handleNetworkErrors (String operation) {
        Log.w(LOG_TAG, "handleNetworkErrors - Enter");
        if (operation.equals("POST")) {
            // TODO buffer usage data for next try
        } else if (operation.equals("GET")) {
            // TODO notify user something wrong happened when searching for new offers
            Intent intent = new Intent("newDataHasArrived");
            intent.putExtra("queryResult","nok");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        } else if (operation.equals("NewOffer")) {
            // TODO notify user something wrong happened when posting his new offer
            Intent intent = new Intent("newDataHasArrived");
            intent.putExtra("queryResult","nok");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        Log.w(LOG_TAG, "handleNetworkErrors - Exit");
    }

}
