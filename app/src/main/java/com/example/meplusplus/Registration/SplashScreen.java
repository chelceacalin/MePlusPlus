package com.example.meplusplus.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.R;


/*
Statuses:
    RFP - ready for production
    TC - to complete ( features to be added )
    NM - needs to be modified/refactored

    Status: RFP
    CREATED DATE: 8/17/2022
    UPDATED DATE: 8/17/2022

    1.
    UPDATED DATE 8/30/2022
    Note: Refactored Notes and assigned variables in init
 */
@SuppressLint("CustomSplashScreen")
@SuppressWarnings("FieldCanBeLocal")
public class SplashScreen extends AppCompatActivity {

    //Timer
    public static long Duration;
    public static long interval;
    public int i;
    public int total;
    //Animatie
    Animation animFadeIn;
    // Sa porneasca splash screen doar odata
    SharedPreferences settings;
    boolean firstRun;
    SharedPreferences.Editor editor;
    //Declarare
    private ImageView imageView;
    private CountDownTimer timer;
    private TextView meplusplus;
    private TextView sp_screen_pls_wait;
    private ProgressBar circularProgressBar;
    private ProgressBar horiontalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
        settings = getSharedPreferences("prefs", 0);
        firstRun = settings.getBoolean("firstRun", false);
        if (!firstRun) {
            editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
            timer = new CountDownTimer(Duration, interval) {
                @Override
                public void onTick(long l) {
                    i += 20;
                    horiontalProgressBar.setProgress(i);
                    circularProgressBar.setProgress(i);
                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.fade_out);
                    finish();
                }
            };
            timer.start();
        } else {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            overridePendingTransition(R.anim.slide_right_to_left_transition, R.anim.slide_left_to_right_transition);
            finish();
        }
    }

    //Initializare Controale
    public void init() {
        //Controale
        imageView = findViewById(R.id.sp_screen_logo);
        circularProgressBar = findViewById(R.id.sp_screen_pg_bar);
        circularProgressBar.setProgress(total);
        horiontalProgressBar = findViewById(R.id.sp_screen_pg_bar_horizontal);
        horiontalProgressBar.setProgress(total);
        meplusplus = findViewById(R.id.meplusplus);
        sp_screen_pls_wait = findViewById(R.id.sp_screen_pls_wait);

        //Animatii
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        //Pornim animatiile cand porneste aplicatia
        meplusplus.setVisibility(View.VISIBLE);
        meplusplus.startAnimation(animFadeIn);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animFadeIn);
        sp_screen_pls_wait.setVisibility(View.VISIBLE);
        sp_screen_pls_wait.startAnimation(animFadeIn);

        //Diverse
        Duration = 2500;
        interval = 600;
        total = 3;
        i = 0;
    }

    @Override
    protected void onDestroy() {
        editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.commit();
        super.onDestroy();

    }

    /*

        timer = new CountDownTimer(Duration, interval) {
            @Override
            public void onTick(long l) {
                i += 20;
                horiontalProgressBar.setProgress(i);
                circularProgressBar.setProgress(i);
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                finish();
            }

        };
        timer.start();*/


}