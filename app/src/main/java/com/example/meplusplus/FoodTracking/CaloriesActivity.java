package com.example.meplusplus.FoodTracking;

import android.content.Intent;
import android.os.Bundle;

import com.example.meplusplus.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.meplusplus.R;

public class CaloriesActivity extends AppCompatActivity {

    //Controale
    ImageView activity_calories_close_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories);
        init();

        activity_calories_close_button.setOnClickListener(view ->{
            startActivity(new Intent(CaloriesActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_right_to_left_transition,R.anim.slide_left_to_right_transition);
            finish();

        });
    }


    private void init(){
        activity_calories_close_button=findViewById(R.id.activity_calories_close_button);
    }

}