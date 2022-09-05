package com.example.meplusplus.FoodTracking;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.meplusplus.DataSets.FoodModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodApiVolley {

    public static final String NUTRIENTS = "https://trackapi.nutritionix.com/v2/natural/nutrients";
    Context context;
    String food_calorie;

    public FoodApiVolley(Context context) {
        this.context = context;
    }

    public void getFood(final String foodName, final NutrientResponse nutrientResponse)

    {

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", foodName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = VolleyMySingleton .getInstance(context).getRequestQueue();

        final List<FoodModel> NutrientReportModel = new ArrayList<>();

        //get json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, NUTRIENTS, postData, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray foods = response.getJSONArray("foods");
                    for (int i = 0; i < foods.length(); i++) {
                        FoodModel Food = new FoodModel();
                        int sum = 0;

                        JSONObject firstFood = (JSONObject) foods.get(i);

                        Food.setName(firstFood.getString("food_name"));
                        Food.setQuantity(firstFood.getInt("serving_qty"));
                        Food.setMeasureUnits(firstFood.getString("serving_unit"));
                        Food.setCalories(firstFood.getLong("nf_calories"));
                        Food.setFats(firstFood.getLong("nf_total_fat"));
                        Food.setCarbs(firstFood.getLong("nf_total_carbohydrate"));
                        Food.setSugar(firstFood.getString("nf_sugars"));
                        Food.setProtein(firstFood.getLong("nf_protein"));
                        NutrientReportModel.add(Food);

                    }


                    nutrientResponse.onSuccess(NutrientReportModel);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                nutrientResponse.onError("Error");

            }



        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-app-id", "761545d2");
                params.put("x-app-key", "9c446a74a908b605a2767a15e244cca3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };

        VolleyMySingleton.getInstance(context).addToRequestQue(request);

    }

   /*     public interface VolleyOnEventListener {
        public void onSuccess(String food_calorie);

        public void onError(String message);
    }

    public void getCalorie(final String foodName, final VolleyOnEventListener volleyOnEventListener) {


        String url = NUTRIENTS;

        JSONObject postData = new JSONObject();
        try {
            postData.put("query", foodName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = MySingleton.getInstance(context).getRequestQueue();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                food_calorie = "";
                try {
                   List<String> list = new ArrayList<String>();
                    JSONArray array = response.getJSONArray("foods");
                    food_calorie = array.getJSONObject(0).getString("nf_calories");
                    volleyOnEventListener.onSuccess(food_calorie);
                    for(int i = 0 ; i < array.length() ; i++){
                       list.add(array.getJSONObject(i).getString("food_name"));
                      list.add(array.getJSONObject(i).getString("nf_calories"));
                   }
                   for(String food_calorie:list){
                        Toast.makeText(context, food_calorie, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyOnEventListener.onError("Failed");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-app-id", "761545d2");
                params.put("x-app-key", "9c446a74a908b605a2767a15e244cca3");
                params.put("x-remote-user-id", "0");
                return params;
            }
        };

        MySingleton.getInstance(context).addToRequestQueue(request);

    }
*/

    public interface NutrientResponse {
        void onSuccess(List<FoodModel> nutrientModels);

        void onError(String message);
    }


}
