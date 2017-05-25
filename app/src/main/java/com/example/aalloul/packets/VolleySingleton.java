package com.example.aalloul.packets;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by adamalloul on 14/04/2017.
 * having a private Context mCtx apparently leads to a memory leak hence the commented parts
 */

class VolleySingleton {

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {mInstance = new VolleySingleton(context);}
        return mInstance;
    }

    RequestQueue getRequestQueue() {return mRequestQueue;}

}
