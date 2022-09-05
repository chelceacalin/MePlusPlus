package com.example.meplusplus.FoodTracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.DataSets.FoodModel;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;

import java.util.List;

public class CaloriesActivity extends AppCompatActivity {

    //Controale
    ImageView activity_calories_close_button;
    EditText activity_calories_items_edit_text;
    Button activity_calories_show_items_button;
    Button activity_calories_add_items;
    ListView SeeItemsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);
        init();


        activity_calories_close_button.setOnClickListener(view -> {
            startActivity(new Intent(CaloriesActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.left, R.anim.fade_out);
            finish();

        });

        activity_calories_show_items_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             FoodApiVolley foodItems = new FoodApiVolley(CaloriesActivity.this);

                foodItems.getFood(activity_calories_items_edit_text.getText().toString(), new FoodApiVolley.NutrientResponse() {
                    @Override
                    public void onSuccess(List<FoodModel> items) {
                        ArrayAdapter<FoodModel> arrayAdapter = new ArrayAdapter<>(CaloriesActivity.this, android.R.layout.simple_list_item_1,   items );
                        SeeItemsListView.setAdapter(arrayAdapter);


                        for(FoodModel item:items){
                            Toast.makeText(CaloriesActivity.this,  item.getName()+item.getCalories(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(CaloriesActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });




    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CaloriesActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.left, R.anim.fade_out);
        finish();
        super.onBackPressed();
    }

    private void init(){
        activity_calories_close_button=findViewById(R.id.activity_calories_close_button);
        activity_calories_items_edit_text=findViewById(R.id.activity_calories_items_edit_text);
        activity_calories_show_items_button=findViewById(R.id.activity_calories_show_items_button);
        activity_calories_add_items=findViewById(R.id.activity_calories_add_items);
        SeeItemsListView= findViewById(R.id.SeeItemsListView);
    }

}
