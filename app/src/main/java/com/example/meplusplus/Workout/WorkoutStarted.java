package com.example.meplusplus.Workout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.asp.fliptimerviewlibrary.CountDownClock;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.ZenMode.ZenModeActivity;

import io.github.muddz.styleabletoast.StyleableToast;

public class WorkoutStarted extends AppCompatActivity {

    // Controale
    ImageView activity_workout_started_close;
    Button activity_workout_started_start_button;
    Button activity_workout_started_add_15_seconds;
    Button activity_workout_started_reset;


    // Rest Timer
    CountDownClock workout_started_countdown;
    long COUNTDOWNTIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_started);
        init();
        workout_started_countdown.resetCountdownTimer();
        activity_workout_started_close.setOnClickListener(view -> {
            startActivity(new Intent(WorkoutStarted.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
            finish();
        });


        activity_workout_started_start_button.setOnClickListener(view -> {
            COUNTDOWNTIME = 1000;
            workout_started_countdown.startCountDown(COUNTDOWNTIME * 60L);


            workout_started_countdown.setCountdownListener(new CountDownClock.CountdownCallBack() {
                @Override
                public void countdownAboutToFinish() {
                }

                @Override
                public void countdownFinished() {
                    new StyleableToast
                            .Builder(WorkoutStarted.this)
                            .text("Good Job!")
                            .textColor(Color.BLACK)
                            .backgroundColor(Color.GREEN)
                            .cornerRadius(25)
                            .iconStart(R.drawable.ic_baseline_check_circle_24)
                            .show();

                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);
                }
            });

        });


        activity_workout_started_add_15_seconds.setOnClickListener(view -> {
            COUNTDOWNTIME += 250;
            workout_started_countdown.startCountDown(COUNTDOWNTIME * 60);
            workout_started_countdown.setCountdownListener(new CountDownClock.CountdownCallBack() {
                @Override
                public void countdownAboutToFinish() {
                }

                @Override
                public void countdownFinished() {
                    new StyleableToast
                            .Builder(WorkoutStarted.this)
                            .text("Good Job!")
                            .textColor(Color.BLACK)
                            .backgroundColor(Color.GREEN)
                            .cornerRadius(25)
                            .iconStart(R.drawable.ic_baseline_check_circle_24)
                            .show();
                        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_INTERCEPT, 650);

                }

            });
        });

        activity_workout_started_reset.setOnClickListener(view -> {
            COUNTDOWNTIME=0;
            workout_started_countdown.resetCountdownTimer();
        });
    }

    private void init() {
        //Controale
        activity_workout_started_close = findViewById(R.id.activity_workout_started_close);
        workout_started_countdown = findViewById(R.id.workout_started_countdown);
        activity_workout_started_start_button = findViewById(R.id.activity_workout_started_start_button);
        activity_workout_started_add_15_seconds = findViewById(R.id.activity_workout_started_add_15_seconds);
        activity_workout_started_reset = findViewById(R.id.activity_workout_started_reset);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WorkoutStarted.this, WorkoutActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }


}