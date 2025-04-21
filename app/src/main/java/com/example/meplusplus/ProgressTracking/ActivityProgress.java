package com.example.meplusplus.ProgressTracking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.example.meplusplus.context.DbContext;
import com.example.meplusplus.model.Weight;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityProgress extends AppCompatActivity {


    //Controale
    ImageView activity_progress_close;
    EditText activity_progress_weight;
    Button activity_progress_add_weight;
    Button activity_progress_reset_weight;
    DatabaseReference reference;
    FirebaseAuth auth;
    String user;

    //Progress
    LineChart lineChart;

    //Divese
    String strWeight;
    Float weight;
    Map<Object, String> map;
    List<Entry> lineEntries;
    long cnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        init();

        activity_progress_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityProgress.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
                finish();
            }
        });

        activity_progress_add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strWeight = activity_progress_weight.getText().toString().trim();
                if (!(strWeight.isEmpty())) {
                    String progressID = reference.push().getKey();
                    map.put("weight", strWeight);
                    user = auth.getCurrentUser().getUid();
                    reference.child(user).child(progressID).setValue(map);
                    activity_progress_weight.setText("");
                    Handler handler = new Handler();
                    handler.postDelayed(ActivityProgress.this::drawGraph, 1000);
                } else {
                    Toast.makeText(ActivityProgress.this, "You must add an weight! ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        activity_progress_reset_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProgress.this);
                        builder.setTitle("Warning !");
                        builder.setMessage("Are you sure you want to delete your progress?");
                        builder.setPositiveButton("YES",(dialog, which) -> {
                           Toast.makeText(ActivityProgress.this, "Progress Has Been Reset", Toast.LENGTH_SHORT).show();
                           activity_progress_weight.setText("");
                            drawGraph();

                        });
                        builder.setNegativeButton("NO",(dialog, which) -> Toast.makeText(ActivityProgress.this, "Reset Cancelled", Toast.LENGTH_SHORT).show());

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });

            }
        });
    }

    private void drawGraph() {
        cnt = 1;
        lineEntries = new ArrayList<>();

        reference.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    float w = Float.parseFloat(ds.getValue(Weight.class).getWeight());
                    lineEntries.add(new Entry(cnt++, w));
                }
                LineDataSet lineDataSet = new LineDataSet(lineEntries, "Progress");
                lineDataSet.setLineWidth(3);
                lineDataSet.setCircleSize(6);
                lineDataSet.setDrawValues(true);
                lineDataSet.setDrawCircles(true);
                lineDataSet.setDrawHighlightIndicators(true);
                lineDataSet.setHighlightEnabled(true);
                lineDataSet.setHighLightColor(Color.CYAN);
                lineDataSet.setValueTextSize(12);
                lineDataSet.setValueTextColor(Color.WHITE);
                lineDataSet.setColor(Color.WHITE);

                LineData lineData = new LineData(lineDataSet);
                lineChart.getDescription().setTextSize(12);
                lineChart.getDescription().setEnabled(false);
                lineChart.animateY(2000);
                lineChart.animateX(2000);
                lineChart.setData(lineData);
                lineChart.getXAxis().setTextColor(Color.WHITE);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getLegend().setTextColor(Color.WHITE);
                lineChart.getAxisLeft().setTextColor(Color.WHITE);
                lineChart.getAxisRight().setEnabled(false);
                lineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void init() {
        //Controale
        activity_progress_close = findViewById(R.id.activity_progress_close);
        activity_progress_weight = findViewById(R.id.activity_progress_weight);
        activity_progress_add_weight = findViewById(R.id.activity_progress_add_weight);
        activity_progress_reset_weight = findViewById(R.id.activity_progress_reset_weight);
        lineChart = findViewById(R.id.activity_progress_graph);
        DbContext dbContext = DbContext.getInstance();

        reference = dbContext.getReference("progress");
        auth = FirebaseAuth.getInstance();

        //Diverse
        map = new HashMap<>();
        cnt = 0;

    }

    @Override
    protected void onStart() {
        drawGraph();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityProgress.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_left_to_right_transition, R.anim.slide_right_to_left_transition);
        finish();
        super.onBackPressed();
    }
}