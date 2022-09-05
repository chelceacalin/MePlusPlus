package com.example.meplusplus.FoodTracking;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyMySingleton {
    @SuppressLint("StaticFieldLeak")
    private static VolleyMySingleton mySingleTon;
    private RequestQueue requestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mctx;
    private VolleyMySingleton(Context context){
        this.mctx=context;
        this.requestQueue=getRequestQueue();

    }
    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized VolleyMySingleton getInstance(Context context){
        if (mySingleTon==null){
            mySingleTon=new VolleyMySingleton(context);
        }
        return mySingleTon;
    }
    public<T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);

    }
}