package com.example.meplusplus.FoodTracking;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyMySingleton {
    @SuppressLint("StaticFieldLeak")
    private static VolleyMySingleton mySingleTon;
    @SuppressLint("StaticFieldLeak")
    private static Context mctx;
    private RequestQueue requestQueue;

    private VolleyMySingleton(Context context) {
        this.mctx = context;
        this.requestQueue = getRequestQueue();

    }

    public static synchronized VolleyMySingleton getInstance(Context context) {
        if (mySingleTon == null) {
            mySingleTon = new VolleyMySingleton(context);
        }
        return mySingleTon;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQue(Request<T> request) {
        requestQueue.add(request);

    }
}