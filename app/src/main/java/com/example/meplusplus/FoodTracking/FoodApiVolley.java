package com.example.meplusplus.FoodTracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

    private final Context context;
    private final String apiURL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    private String foodCalorie;
    private JSONObject data;
    private RequestQueue requestQueue;
    private List<FoodModel> foodItems;
    private JsonObjectRequest request;
    private FoodModel foodItem;

    public FoodApiVolley(Context context) {
        this.context = context;
    }

    public void search(String name) {
        requestQueue = VolleyMySingleton.getInstance(context).getRequestQueue();
        data = new JSONObject();
        try {
            data.put("query", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        foodItems = new ArrayList<>();

        request = new JsonObjectRequest(Request.Method.POST, apiURL, data, new Response.Listener<JSONObject>() {

            @SuppressLint("ApplySharedPref")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray foods = response.getJSONArray("foods");
                    for (int i = 0; i < foods.length(); i++) {
                        JSONObject foodJson = foods.getJSONObject(i);
                        foodItem = new FoodModel(
                                foodJson.getString("food_name"),
                                foodJson.getLong("nf_calories"),
                                foodJson.getInt("serving_qty"),
                                foodJson.getString("serving_unit"),
                                foodJson.getLong("nf_total_fat"),
                                foodJson.getLong("nf_total_carbohydrate"),
                                foodJson.getLong("nf_protein"),
                                foodJson.getLong("nf_sugars")
                        );
                        foodItems.add(foodItem);
                    }

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(foodItems);
                    editor.putString("MYITEMS", json);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                new StyleableToast.Builder(context)
                        .text("Server unavailable right now !")
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
                params.put("x-app-id", "761545d2");
                params.put("x-app-key", "9c446a74a908b605a2767a15e244cca3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };
        VolleyMySingleton.getInstance(context).addToRequestQue(request);
    }
}
