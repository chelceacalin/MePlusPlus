package com.example.meplusplus.Workout;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.meplusplus.R;
public class WorkoutActivity extends AppCompatActivity {

        //Controale
    ImageView activity_workout_close;
    RecyclerView activity_workout_recycler_view;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        init();
    }

    private void init(){
        activity_workout_close = findViewById(R.id.activity_workout_close);
        activity_workout_recycler_view=findViewById(R.id.activity_workout_recycler_view);
        manager=new LinearLayoutManager(WorkoutActivity.this);
        activity_workout_recycler_view.setLayoutManager(manager);
    }
}