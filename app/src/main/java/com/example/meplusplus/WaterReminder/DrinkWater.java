package com.example.meplusplus.WaterReminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrinkWater extends AppCompatActivity {


    SeekBar activity_drink_water_seekbar;
    TextView activity_drink_water_set_your_goals;
    TextView activity_drink_water_goal_water;

    Button activity_drink_water_modify_your_goals;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    //Sum water drank
    int sum = 0;
    int total = 0;

    Integer waterDrank=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_water);
        init();

        activity_drink_water_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                activity_drink_water_goal_water.setText(String.valueOf(progress));
                waterDrank=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("ApplySharedPref")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            //alert dialog cu salvare
                AlertDialog alertDialog= new AlertDialog.Builder(DrinkWater.this).create();
                alertDialog.setTitle("Do you want to add a new water goal?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "NO", (dialog, which) -> dialog.dismiss());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", (dialog, which) -> {
                    SharedPreferences sharedPref1 = getSharedPreferences("msp", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref1.edit();
                    editor.clear();
                    editor.commit();
                    dialog.dismiss();


                  sharedPref1 = DrinkWater.this.getSharedPreferences("msp", Context.MODE_PRIVATE);
                   editor = sharedPref1.edit();
                    editor.putInt("waterDrank", waterDrank);
                    editor.apply();
                });
                alertDialog.show();
            }
        });

        activity_drink_water_modify_your_goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activity_drink_water_set_your_goals.getVisibility() == View.VISIBLE && activity_drink_water_seekbar.getVisibility() == View.VISIBLE
                        || activity_drink_water_goal_water.getVisibility() == View.VISIBLE) {
                    activity_drink_water_set_your_goals.setVisibility(View.GONE);
                    activity_drink_water_seekbar.setVisibility(View.GONE);
                    activity_drink_water_goal_water.setVisibility(View.GONE);
                } else {
                    activity_drink_water_set_your_goals.setVisibility(View.VISIBLE);
                    activity_drink_water_seekbar.setVisibility(View.VISIBLE);
                    activity_drink_water_goal_water.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void init() {
       //Controale
        activity_drink_water_modify_your_goals = findViewById(R.id.activity_drink_water_modify_your_goals);
        activity_drink_water_set_your_goals = findViewById(R.id.activity_drink_water_set_your_goals);
        activity_drink_water_goal_water = findViewById(R.id.activity_drink_water_goal_water);
        activity_drink_water_seekbar = findViewById(R.id.activity_drink_water_seekbar);


        //Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();
    }


}