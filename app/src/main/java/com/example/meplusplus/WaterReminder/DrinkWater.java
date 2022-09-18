package com.example.meplusplus.WaterReminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.ToDo.ToDoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrinkWater extends AppCompatActivity {

    //Controale
    SeekBar activity_drink_water_seekbar;
    TextView activity_drink_water_set_your_goals;
    TextView activity_drink_water_goal_water;
    ImageView activity_drink_water_close;
    Button activity_drink_water_modify_your_goals;
    RadioGroup activity_drink_water_radiogroup;
    RadioButton activity_drink_water_ofcourse;
    RadioButton activity_drink_water_nothankyou;
    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    //Diverse
    Integer waterDrank = 0;
    int apasat = 0;

    //Shared Preferences pt reminder
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_water);
        init();
        prefs = getSharedPreferences("wantReminders", MODE_PRIVATE);
        boolean wantReminders = prefs.getBoolean("yesToReminders", false);
        activity_drink_water_ofcourse.setChecked(wantReminders);
        activity_drink_water_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                activity_drink_water_goal_water.setText(String.valueOf(progress));
                waterDrank = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("ApplySharedPref")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //alert dialog cu salvare
                AlertDialog alertDialog = new AlertDialog.Builder(DrinkWater.this).create();
                alertDialog.setTitle("Do you want to add a set water goal?");
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

        //RadioGroup
        activity_drink_water_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i) {
                    case R.id.activity_drink_water_ofcourse:
                        apasat = 1;
                        editor = getSharedPreferences("wantReminders", MODE_PRIVATE).edit();
                        editor.putBoolean("yesToReminders", true);
                        editor.apply();


                        break;
                    case R.id.activity_drink_water_nothankyou:
                        apasat = 2;

                        SharedPreferences sharedPref1 = getSharedPreferences("wantReminders", MODE_PRIVATE);
                        editor = sharedPref1.edit();
                        editor.clear();
                        editor.commit();
                        break;

                }
            }
        });

        activity_drink_water_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DrinkWater.this, MainActivity.class);
                intent.putExtra("MetabolismToMeFragment", true);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                finish();
                startActivity(intent);
            }
        });


    }

    private void init() {
        //Controale
        activity_drink_water_modify_your_goals = findViewById(R.id.activity_drink_water_modify_your_goals);
        activity_drink_water_set_your_goals = findViewById(R.id.activity_drink_water_set_your_goals);
        activity_drink_water_goal_water = findViewById(R.id.activity_drink_water_goal_water);
        activity_drink_water_seekbar = findViewById(R.id.activity_drink_water_seekbar);
        activity_drink_water_close = findViewById(R.id.activity_drink_water_close);


        //Radiogroup
        activity_drink_water_radiogroup = findViewById(R.id.activity_drink_water_radiogroup);
        activity_drink_water_ofcourse = findViewById(R.id.activity_drink_water_ofcourse);
        activity_drink_water_nothankyou = findViewById(R.id.activity_drink_water_nothankyou);


        //Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DrinkWater.this, MainActivity.class);
        intent.putExtra("MetabolismToMeFragment", true);
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
        finish();
        startActivity(intent);
    }
}