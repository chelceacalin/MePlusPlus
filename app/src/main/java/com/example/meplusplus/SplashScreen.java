package com.example.meplusplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.meplusplus.R;

// RFP
   /*
       CREATED DATE: 8/17/2022
       UPDATED DATE: 8/17/2022
    */
public class SplashScreen extends AppCompatActivity {

    //Animatie
    Animation animFadeIn;
    public static final long Duration=2500; // 2.5 s
    public static long interval=600; //250 ms
    public int i=0,total=3;
    //Declarare
    private ImageView imageView;
    private CountDownTimer timer;
    private TextView meplusplus,sp_screen_pls_wait;
    private ProgressBar circularProgressBar,horiontalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();

        timer=new CountDownTimer(Duration,interval) {
            @Override
            public void onTick(long l) {
                i+=20;
                horiontalProgressBar.setProgress(i);
                circularProgressBar.setProgress(i);
            }
            @Override
            public void onFinish() {
                startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                overridePendingTransition(R.anim.fade_in,R.anim.slide_out);
                finish();
            }
        };
        timer.start();
    }

    //Initializare Controale
   public void init(){
        imageView=findViewById(R.id.sp_screen_logo);
        circularProgressBar =findViewById(R.id.sp_screen_pg_bar);
        circularProgressBar.setProgress(total);
        horiontalProgressBar=findViewById(R.id.sp_screen_pg_bar_horizontal);
        horiontalProgressBar.setProgress(total);
        meplusplus=findViewById(R.id.meplusplus);
        sp_screen_pls_wait=findViewById(R.id.sp_screen_pls_wait);


       animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
               R.anim.fade_in);

       //Pornim animatiile cand porneste aplicatia
       meplusplus.setVisibility(View.VISIBLE);
       meplusplus.startAnimation(animFadeIn);
       imageView.setVisibility(View.VISIBLE);
       imageView.startAnimation(animFadeIn);
       sp_screen_pls_wait.setVisibility(View.VISIBLE);
       sp_screen_pls_wait.startAnimation(animFadeIn);
    }

    //Cand apesi pe back cand esti pe pagina de login
    @Override
    public void onBackPressed() {
        System.exit(0);
        finish();
        super.onBackPressed();
    }

}