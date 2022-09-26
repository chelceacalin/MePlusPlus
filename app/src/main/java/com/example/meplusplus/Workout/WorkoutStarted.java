package com.example.meplusplus.Workout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

    //Diverse
    String ItemName;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            ItemName = intent.getStringExtra("schimba");
            if (ItemName != null) {
                if (ItemName.equals("startAgain")) {
                    activity_workout_started_start_button.performClick();
                }
            }

        }
    };
    //Sa fol acelasi recyclerview pt toate
    Bundle extras;
    String workoutName;
    String tempWorkoutName = "";
    int contor=0;


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


        preferences = getSharedPreferences("longclickview", MODE_PRIVATE);

        activity_workout_started_reset.setOnClickListener(view -> {
            COUNTDOWNTIME = 0;
            workout_started_countdown.resetCountdownTimer();
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


        readExercises();
    }

    private void init() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-message".
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));

        //Controale
        activity_workout_started_close = findViewById(R.id.activity_workout_started_close);
        workout_started_countdown = findViewById(R.id.workout_started_countdown);
        activity_workout_started_start_button = findViewById(R.id.activity_workout_started_start_button);
        activity_workout_started_add_15_seconds = findViewById(R.id.activity_workout_started_add_15_seconds);
        activity_workout_started_reset = findViewById(R.id.activity_workout_started_reset);

        //Recyclerview
        activity_workout_started_recyclerview = findViewById(R.id.activity_workout_started_recyclerview);
        manager = new LinearLayoutManager(WorkoutStarted.this);
        activity_workout_started_recyclerview.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new ExerciseAdapter(WorkoutStarted.this, list);

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

    private void readExercises() {
        extras = getIntent().getExtras();
        if (extras != null) {
            workoutName = extras.getString("Push");
            if (workoutName != null) {
                tempWorkoutName = workoutName;
            } else {
                workoutName = extras.getString("Pull");
                if (workoutName != null) {
                    tempWorkoutName = workoutName;
                } else {
                    workoutName = extras.getString("Legs");
                    if (workoutName != null) {
                        tempWorkoutName = workoutName;
                    } else {
                        workoutName = extras.getString("Upper");
                        if (workoutName != null) {
                            tempWorkoutName = workoutName;
                        } else {
                            workoutName = extras.getString("Lower");
                            if (workoutName != null) {
                                tempWorkoutName = workoutName;
                            } else {
                                workoutName = extras.getString("Full Body A");
                                if (workoutName != null) {
                                    tempWorkoutName = workoutName;
                                } else {
                                    workoutName = extras.getString("Full Body B");
                                    if (workoutName != null) {
                                        tempWorkoutName = workoutName;
                                    } else {

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                contor=0;
                for (DataSnapshot sn : snapshot.getChildren()) {
                    contor++;

                    if (tempWorkoutName.equals("Upper")) {
                        if (sn.getValue(Exercise.class).getMainSplit().equals("Push")
                                || sn.getValue(Exercise.class).getExercise_name().equals("Dumbbell Hammer Curl") ||
                                sn.getValue(Exercise.class).getExercise_name().equals("Dumbbell Curl") ||
                                sn.getValue(Exercise.class).getExercise_name().equals("Cable Curl")) {
                            list.add(sn.getValue(Exercise.class));
                        }

                    } else if (tempWorkoutName.equals("Lower")) {
                        if (sn.getValue(Exercise.class).getMainSplit().equals("Legs") || sn.getValue(Exercise.class).getMainSplit().equals("Core")) {
                            list.add(sn.getValue(Exercise.class));
                        }
                    }
                    else if (tempWorkoutName.equals("Full Body B")) {
                        if(contor%3==0)
                            list.add(sn.getValue(Exercise.class));
                    }
                    else if (tempWorkoutName.equals("Full Body A")) {
                       if(contor%2==0&&contor<=16)
                            list.add(sn.getValue(Exercise.class));
                    }
                    else if (sn.getValue(Exercise.class).getMainSplit().equals(tempWorkoutName)
                            && !(tempWorkoutName.equals("Upper"))
                            && !(tempWorkoutName.equals("Lower"))
                            && !(tempWorkoutName.equals("Full Body A"))
                            && !(tempWorkoutName.equals("Full Body B"))) {
                        list.add(sn.getValue(Exercise.class));
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}