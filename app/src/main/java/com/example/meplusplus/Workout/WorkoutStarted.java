package com.example.meplusplus.Workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asp.fliptimerviewlibrary.CountDownClock;
import com.example.meplusplus.Adapters.ExerciseAdapter;
import com.example.meplusplus.DataSets.Exercise;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.github.muddz.styleabletoast.StyleableToast;

public class WorkoutStarted extends AppCompatActivity {

    // Controale
    ImageView activity_workout_started_close;
    Button activity_workout_started_start_button;
    Button activity_workout_started_add_15_seconds;
    Button activity_workout_started_reset;

    //Recyclerview
    RecyclerView activity_workout_started_recyclerview;
    LinearLayoutManager manager;
    ExerciseAdapter adapter;
    List<Exercise> list;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    // Rest Timer
    CountDownClock workout_started_countdown;
    long COUNTDOWNTIME = 0;

    //Shared Preferences ca sa resetam cand dam long click
    SharedPreferences preferences;

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




        readExercises();
    }

    private void init() {
        //Controale
        activity_workout_started_close = findViewById(R.id.activity_workout_started_close);
        workout_started_countdown = findViewById(R.id.workout_started_countdown);
        activity_workout_started_start_button = findViewById(R.id.activity_workout_started_start_button);
        activity_workout_started_add_15_seconds = findViewById(R.id.activity_workout_started_add_15_seconds);
        activity_workout_started_reset = findViewById(R.id.activity_workout_started_reset);

        //Recyclerview
        activity_workout_started_recyclerview=findViewById(R.id.activity_workout_started_recyclerview);
        manager=new LinearLayoutManager(WorkoutStarted.this);
        activity_workout_started_recyclerview.setLayoutManager(manager);
        list=new ArrayList<>();
        adapter=new ExerciseAdapter(WorkoutStarted.this, list);

        activity_workout_started_recyclerview.setAdapter(adapter);


        //Firebase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("exercise");

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WorkoutStarted.this, WorkoutActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }

    private void readExercises(){
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot sn:snapshot.getChildren()){
                    list.add(sn.getValue(Exercise.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}