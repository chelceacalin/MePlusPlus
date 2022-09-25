package com.example.meplusplus.Workout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.Adapters.WorkoutAdapter;
import com.example.meplusplus.Chatting.ChattingActivity;
import com.example.meplusplus.DataSets.Workout_Split;
import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    //Controale
    ImageView activity_workout_close;
    RecyclerView activity_workout_recycler_view;
    LinearLayoutManager manager;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    // Recycler
    WorkoutAdapter adapter;
    List<Workout_Split> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        init();

        activity_workout_close.setOnClickListener(view -> {
            startActivity(new Intent(WorkoutActivity.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_left_to_right_transition,R.anim.slide_right_to_left_transition);
            finish();
            super.onBackPressed();

        });

        readWorkouts();
    }

    private void readWorkouts() {
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    list.add(ds.getValue(Workout_Split.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(){
        //Controale
        activity_workout_close = findViewById(R.id.activity_workout_close);


        //Firebase
        database = FirebaseDatabase.getInstance("https://meplusplus-d17e9-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("workout");

       //RecyclerView
        activity_workout_recycler_view=findViewById(R.id.activity_workout_recycler_view);
        manager=new LinearLayoutManager(WorkoutActivity.this);
        activity_workout_recycler_view.setLayoutManager(manager);
        list=new ArrayList<>();
        adapter=new WorkoutAdapter(WorkoutActivity.this, list);
        activity_workout_recycler_view.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(WorkoutActivity.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition,R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();

    }
}