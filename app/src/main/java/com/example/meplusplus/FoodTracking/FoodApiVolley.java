package com.example.meplusplus.FoodTracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.meplusplus.DataSets.FoodModel;
import com.example.meplusplus.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;

public class FoodApiVolley {

    final Context context;
    final String ApiURL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    String food_calorie;
    JSONObject data;
    RequestQueue q;
    List<FoodModel> items;
    JsonObjectRequest req;
    JSONArray foodItems;
    FoodModel fooditem;

    public FoodApiVolley(Context context) {
        this.context = context;
    }

    public void search(String name) {
        q = VolleyMySingleton.getInstance(context).getRequestQueue();
        data = new JSONObject();
        try {
            data.put("query", name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        items = new ArrayList<>();

        req = new JsonObjectRequest(Request.Method.POST, ApiURL, data, new Response.Listener<JSONObject>() {

            @SuppressLint("ApplySharedPref")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    foodItems = response.getJSONArray("foods");
                    for (int i = 0; i < foodItems.length(); i++) {
                        fooditem = new FoodModel(((JSONObject) foodItems.get(i)).getString("food_name"), ((JSONObject) foodItems.get(i)).getLong("nf_calories"), ((JSONObject) foodItems.get(i)).getInt("serving_qty"),
                                ((JSONObject) foodItems.get(i)).getString("serving_unit"), ((JSONObject) foodItems.get(i)).getLong("nf_total_fat"), ((JSONObject) foodItems.get(i)).getLong("nf_total_carbohydrate"), ((JSONObject) foodItems.get(i)).getLong("nf_protein"),
                                ((JSONObject) foodItems.get(i)).getLong("nf_sugars"));
                        items.add(fooditem);
                    }

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPrefs.edit();

                    Gson gson = new Gson();
                    String json = gson.toJson(items);
                    editor.putString("MYITEMS", json);
                    editor.commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new StyleableToast.Builder(context)
                        .text("You must complete all the fields")
                        .textColor(Color.RED)
                        .backgroundColor(context.getResources().getColor(R.color.WhiteSmoke))
                        .cornerRadius(25)
                        .iconStart(R.drawable.ic_baseline_error_outline_24)
                        .show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                String APP_KEY = "null";
                String APP_ID="null";
                try {
                    ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

                    Bundle value=info.metaData;
                    APP_KEY= (String) value.get("com.google.android.geo.API_KEY");
                    APP_ID= (String) value.get("com.google.android.geo.API_ID");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                params.put("x-app-id", APP_KEY);
                params.put("x-app-key", APP_ID);
                params.put("x-remote-user-id", "0");
                return params;
            }
        };
        VolleyMySingleton.getInstance(context).addToRequestQue(req);
    }

}
