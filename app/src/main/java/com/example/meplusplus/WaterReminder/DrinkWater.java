package com.example.meplusplus.WaterReminder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DrinkWater extends AppCompatActivity implements SensorEventListener {

    //Controale
    SeekBar activity_drink_water_seekbar;
    TextView activity_drink_water_set_your_goals;
    TextView activity_drink_water_goal_water;
    ImageView activity_drink_water_close;
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




    //  Steps counter
    private TextView stepCountTextView;
    private TextView calories_burned;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private int stepCount = 0;
    private boolean isSensorRegistered = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_water);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){ //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
            }
        }
        init();


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Get reference to the step sensor if available
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            Toast.makeText(this, "Step sensor not available", Toast.LENGTH_SHORT).show();
        }

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
                startActivity(new Intent(DrinkWater.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
                finish();
            }
        });


    }

    private void init() {
        //Controale
        activity_drink_water_set_your_goals = findViewById(R.id.activity_drink_water_set_your_goals);
        activity_drink_water_goal_water = findViewById(R.id.activity_drink_water_goal_water);
        activity_drink_water_seekbar = findViewById(R.id.activity_drink_water_seekbar);
        activity_drink_water_close = findViewById(R.id.activity_drink_water_close);

        // Get reference to the UI components
        stepCountTextView = findViewById(R.id.step_count);
        calories_burned=findViewById(R.id.calories_burned);


        //Radiogroup
        activity_drink_water_radiogroup = findViewById(R.id.activity_drink_water_radiogroup);
        activity_drink_water_ofcourse = findViewById(R.id.activity_drink_water_ofcourse);
        activity_drink_water_nothankyou = findViewById(R.id.activity_drink_water_nothankyou);


        //Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://applicenta-8582b-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(DrinkWater.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register the step sensor listener
        if (stepSensor != null && !isSensorRegistered) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            isSensorRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the step sensor listener
        if (stepSensor != null && isSensorRegistered) {
            sensorManager.unregisterListener(this, stepSensor);
            isSensorRegistered = false;
        }
    }
int i=1;
    @SuppressLint({"SetTextI18n", "SuspiciousIndentation"})
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // Update the step count
            stepCount = (int) event.values[0];

            if(stepCount<1000)
            stepCountTextView.setTextColor(Color.BLUE);

            if(stepCount>8000){
                stepCountTextView.setTextColor(Color.GREEN);
            }

            if(stepCount>10000&stepCount<20000)
                stepCount/=2;
            if(stepCount>20000&stepCount<30000)
                stepCount/=4;

            if(stepCount>30000)
                stepCount/=6;

            stepCountTextView.setText(String.valueOf(stepCount));
            int caloriesB=(int) ((double)stepCount*0.063);
            calories_burned.setText(caloriesB+"");



        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }
}