package com.example.meplusplus.Workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.WorkoutAdapter;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.example.meplusplus.model.Workout_Split;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    //Controale
    ImageView activity_workout_close;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch activity_workout_switch_compact_mode;
    RecyclerView activity_workout_recycler_view;
    LinearLayoutManager manager;

    //Firebase
    DbContext dbContext = DbContext.getInstance();
    DatabaseReference reference;

    // Recycler
    WorkoutAdapter adapter;
    List<Workout_Split> list;


    //Shared Preferences pt padding
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        init();

        activity_workout_close.setOnClickListener(view -> {
            startActivity(new Intent(WorkoutActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
            finish();
            super.onBackPressed();

        });
        SharedPreferences switchButton = getSharedPreferences("wantPadding", MODE_PRIVATE);
        boolean switchStatus = switchButton.getBoolean("yes", false);
        activity_workout_switch_compact_mode.setChecked(switchStatus);


        activity_workout_switch_compact_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean istoggled) {
                editor = getSharedPreferences("wantPadding", MODE_PRIVATE).edit();
                editor.clear();
                editor.putBoolean(istoggled ? "yes" : "no", true);
                editor.apply();
                adapter.notifyDataSetChanged();
            }
        });


        readWorkouts();
    }

    private void readWorkouts() {
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    list.add(ds.getValue(Workout_Split.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {

        //Controale
        activity_workout_close = findViewById(R.id.activity_workout_close);
        activity_workout_switch_compact_mode = findViewById(R.id.activity_workout_switch_compact_mode);


        //Firebase
        reference = dbContext.getReference("workout");

        //RecyclerView
        activity_workout_recycler_view = findViewById(R.id.activity_workout_recycler_view);
        manager = new LinearLayoutManager(WorkoutActivity.this);
        activity_workout_recycler_view.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new WorkoutAdapter(WorkoutActivity.this, list);
        activity_workout_recycler_view.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(WorkoutActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();

    }
}