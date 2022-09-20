package com.example.meplusplus.FoodTracking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.CalorieCalculator.CalculateMetabolismActivity;
import com.example.meplusplus.DataSets.FoodModel;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaloriesActivity extends AppCompatActivity {

    //Controale
    ImageView activity_calories_close_button;
    EditText activity_calories_items_edit_text;
    Button activity_calories_search_items;
    Button activity_calories_add_items;
    Button activity_calories_show_items;
    ListView SeeItemsListView;

    //Preferences for list and GSon and stuff to read arraylist
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    List<FoodModel> arrayList;
    ArrayAdapter<FoodModel> arrayAdapter;
    FoodApiVolley foodItems;

    //Calculam Caloriile
    TextView activity_calories_total_calories;
    TextView activity_calories_protein;
    TextView activity_calories_carbs;
    TextView activity_calories_fats;
    TextView activity_calories_sugar;
    float sumCalories,sumProtein,sumCarbs,sumFats,sumSugar;
     //Sa le trimitem la activitatea principala
    String foodItemsSearched;

String user;
    //Metoda 2
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);
        init();

        activity_calories_close_button.setOnClickListener(view -> {
            // Schimbam din activitate in fragment
            Intent intent = new Intent(CaloriesActivity.this, MainActivity.class);
            intent.putExtra("MetabolismToMeFragment", true);
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
            finish();
            startActivity(intent);


        });

        activity_calories_search_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Daca stergi shared preferences aici o sa se blocheze

                foodItems= new FoodApiVolley(CaloriesActivity.this);
                sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CaloriesActivity.this);
                gson = new Gson();
                json = sharedPrefs.getString("MYITEMS", "");
                type = new TypeToken<List<FoodModel>>() {}.getType();
                foodItemsSearched=activity_calories_items_edit_text.getText().toString();
                if(foodItemsSearched.equals("")){
                    Toast.makeText(CaloriesActivity.this, "You have to add items", Toast.LENGTH_SHORT).show();
                }
                else
                foodItems.search(activity_calories_items_edit_text.getText().toString());

                arrayList = gson.fromJson(json, type);



            }
        });

        activity_calories_show_items.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                activity_calories_search_items.performClick();

                //PT ListView
                if(arrayList!=null){
                    arrayAdapter = new ArrayAdapter<>(CaloriesActivity.this, android.R.layout.simple_list_item_1,arrayList );
                    SeeItemsListView.setAdapter(arrayAdapter);

                    sumCalories=0;sumProtein=0;sumCarbs=0;sumFats=0;sumSugar=0;
                    for (FoodModel item:arrayList){
                        sumCalories+=item.getCalories();
                        sumProtein+=item.getProtein();
                        sumCarbs+=item.getCarbs();
                        sumFats=item.getFats();
                        sumSugar+=item.getSugar();
                    }

                    activity_calories_total_calories.setText(sumCalories+"");
                    activity_calories_protein.setText(sumProtein+"");
                    activity_calories_carbs.setText(sumCarbs+"");
                    activity_calories_fats.setText(sumFats+"");
                    activity_calories_sugar.setText(sumSugar+"");
                }


                //Calculezi suma itemetelor din listview



            }
        });

        activity_calories_add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user= FirebaseAuth.getInstance().getCurrentUser().getUid();
                String itemID=reference.push().getKey();
                Map<String,Object> map=new HashMap<>();
                map.put("itemID",itemID);
                map.put("sumCalories",sumCalories);
                map.put("sumProtein",sumProtein);
                map.put("sumCarbs",sumCarbs);
                map.put("sumFats",sumFats);
                map.put("sumSugar",sumSugar);

                reference.child(user).child(itemID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Schimbam din activitate in fragment
                        Intent intent = new Intent(CaloriesActivity.this, MainActivity.class);
                        intent.putExtra("MetabolismToMeFragment", true);
                        intent.putExtra("itemID",itemID);
                        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                        finish();
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CaloriesActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });





            }
        });



    }


    private void init(){
        //Controale
        activity_calories_close_button=findViewById(R.id.activity_calories_close_button);
        activity_calories_items_edit_text=findViewById(R.id.activity_calories_items_edit_text);
        activity_calories_search_items=findViewById(R.id.activity_calories_search_items);
        activity_calories_add_items=findViewById(R.id.activity_calories_add_items);
        activity_calories_show_items=findViewById(R.id.activity_calories_show_items);
        SeeItemsListView= findViewById(R.id.SeeItemsListView);


        //Calories related
        activity_calories_total_calories=findViewById(R.id.activity_calories_total_calories);
        activity_calories_total_calories.setText("0");
        activity_calories_protein=findViewById(R.id.activity_calories_protein);
        activity_calories_protein.setText("0");
        activity_calories_carbs=findViewById(R.id.activity_calories_carbs);
        activity_calories_carbs.setText("0");
        activity_calories_fats=findViewById(R.id.activity_calories_fats);
        activity_calories_fats.setText("0");
        activity_calories_sugar=findViewById(R.id.activity_calories_sugar);
        activity_calories_sugar.setText("0");

        sumCalories=0;sumProtein=0;sumCarbs=0;sumFats=0;sumSugar=0;
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("foods");

    }


    @Override
    public void onBackPressed() {
        // Schimbam din activitate in fragment
        Intent intent = new Intent(CaloriesActivity.this, MainActivity.class);
        intent.putExtra("MetabolismToMeFragment", true);
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        finish();
        startActivity(intent);
    }


}
