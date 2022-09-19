package com.example.meplusplus.ProgressTracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.MainActivity;
import com.example.meplusplus.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ActivityProgress extends AppCompatActivity {


    //Controale
    ImageView activity_progress_close;
    GraphView activity_progress_graph;
    EditText activity_progress_weight;
    Button activity_progress_add_weight;
    Button activity_progress_reset_weight;

    //Divese
    String strWeight;
    Float weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        init();
        drawGraph();
        activity_progress_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityProgress.this, MainActivity.class);
                intent.putExtra("MetabolismToMeFragment", true);
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out);
                finish();
                startActivity(intent);
            }
        });


        activity_progress_add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strWeight = activity_progress_weight.getText().toString().trim();
                if (!(strWeight.equals(""))) {
                    weight = Float.parseFloat(strWeight);
                    drawGraph();
                } else {
                    Toast.makeText(ActivityProgress.this, "You must add an weight! ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        activity_progress_reset_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void drawGraph() {


        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 66),
                new DataPoint(1, 67),
                new DataPoint(2, 69),
                new DataPoint(3, 66.7),
                new DataPoint(4, 70.2)
        });
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(13);
        activity_progress_graph.setTitle(" Weight Tracker");
        activity_progress_graph.setTitleTextSize(80);
        activity_progress_graph.addSeries(series);


            /*
i=0
            for Datasnapshot ds:snapshor
              series.appendata(new DataPoint(i++,ds.getweight),true,15)

             */
    }

    private void init() {
        activity_progress_close = findViewById(R.id.activity_progress_close);
        activity_progress_graph = findViewById(R.id.activity_progress_graph);
        activity_progress_weight = findViewById(R.id.activity_progress_weight);
        activity_progress_add_weight = findViewById(R.id.activity_progress_add_weight);
        activity_progress_reset_weight=findViewById(R.id.activity_progress_reset_weight);
    }
}