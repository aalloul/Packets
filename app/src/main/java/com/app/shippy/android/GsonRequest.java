package com.app.shippy.android;


import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by adamalloul on 14/04/2017.
 * Imported from http://stackoverflow.com/questions/36432152/android-volley-gson-post
 */

class GsonRequest<T> extends Request<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final Object dataIn;
    private final static String LOG_TAG = GsonRequest.class.getSimpleName();
    private final static boolean DEBUG = false;

    /**
     * Make a request and return a parsed object from JSON.
     * We allow for ArrayList of HashMaps and also a single HashMap
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    GsonRequest(String url, ArrayList<HashMap<String, Object>> dataIn, Class<T> clazz,
                Map<String, String> headers, int method, Response.Listener<T> listener,
                Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.dataIn = dataIn;
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        if (DEBUG) Log.i(LOG_TAG, "GsonRequest - Constructed with ArrayList");
    }

    GsonRequest(String url, HashMap<String, Object> dataIn, Class<T> clazz,
                Map<String, String> headers, int method, Response.Listener<T> listener,
                Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        this.dataIn = dataIn;
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        if (DEBUG) Log.i(LOG_TAG, "GsonRequest - Constructed with HashMap");
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (DEBUG) Log.i(LOG_TAG, "getHeaders - Enter");
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        if (DEBUG) Log.i(LOG_TAG, "deliverResponse - Enter");
        listener.onResponse(response);
        if (DEBUG) Log.i(LOG_TAG, "deliverResponse - Exit");
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (DEBUG) Log.i(LOG_TAG, "getBody - Enter");
        try {
            return gson.toJson(dataIn).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is called to parse the NetworkResponse --
     *   if the method is a POST, we ignore the response from the back-end as it's supposed to be
     *   an HTTP 200 type.
     *   if the method is a GET, it means we're trying to get new offers and the response should be
     *   parsed accordingly
     * @param response
     * @return the parsed response
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (DEBUG) Log.i(LOG_TAG, "parseNetworkResponse - Enter");

        // if clazz is null then it means we just don't care about parsing the response
        if (clazz == null) return (Response.success(null,HttpHeaderParser.parseCacheHeaders(response) ));

        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if (DEBUG) Log.i(LOG_TAG, "parseNetworkResponse - json = " + json);

            if (json.equals("{}")) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }

            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            if (DEBUG) Log.i(LOG_TAG, "parseNetworkResponse - JsonSyntaxException");
            return Response.error(new ParseError(e));
        }
    }
}
