package com.example.meplusplus.CalorieCalculator;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meplusplus.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class CalculateMetabolismActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Controale
    RadioGroup activity_calculate_metabolism_radiogroup;
    RadioButton activity_calculate_metabolism_option_male;
    RadioButton activity_calculate_metabolism_option_female;
        Spinner activity_calculate_metabolism_spinner;
    Button activity_calculate_metabolism_button_calculate;
    Button activity_calculate_metabolism_save;
    //Metabolism Numbers
    EditText activity_calculate_metabolism_height;
    EditText activity_calculate_metabolism_weight;
    EditText activity_calculate_metabolism_age;

    TextView activity_calculate_metabolism_caloriestoconsume;
    Integer  height,age;
    Float weight;
    boolean isMale;
    int apasat=0;
    int nivelactivitate=0;
    Float volumactivitate=0f;


    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_metabolism);
        init();

        //Pt Spinner
            ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(CalculateMetabolismActivity.this,R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activity_calculate_metabolism_spinner.setAdapter(adapter);
        activity_calculate_metabolism_spinner.setOnItemSelectedListener(CalculateMetabolismActivity.this);

        //RadioGroup
        activity_calculate_metabolism_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){
                    case R.id.activity_calculate_metabolism_option_male:
                        isMale=true;
                        apasat = 1;
                        break;
                    case R.id.activity_calculate_metabolism_option_female:
                        isMale=false;
                        apasat=1;
                        break;

                }
            }
        });

        activity_calculate_metabolism_button_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(activity_calculate_metabolism_age.getText().toString().equals("") || activity_calculate_metabolism_weight.getText().toString().equals("")
                        ||activity_calculate_metabolism_height.getText().toString().equals("")|| apasat==0||nivelactivitate==0){
                    Toast.makeText(CalculateMetabolismActivity.this, "You must complete all the fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    age=Integer.parseInt(activity_calculate_metabolism_age.getText().toString().trim());
                    height=Integer.parseInt(activity_calculate_metabolism_height.getText().toString().trim());
                    weight=Float.parseFloat(activity_calculate_metabolism_weight.getText().toString().trim());
                    //Piechart

                    showPieChart();
                }
            }
        });
    }


    private void init(){
        //Controale
        activity_calculate_metabolism_radiogroup=findViewById(R.id.activity_calculate_metabolism_radiogroup);
        activity_calculate_metabolism_option_male=findViewById(R.id.activity_calculate_metabolism_option_male);
        activity_calculate_metabolism_option_female=findViewById(R.id.activity_calculate_metabolism_option_female);
        activity_calculate_metabolism_spinner=findViewById(R.id.activity_calculate_metabolism_spinner);
        activity_calculate_metabolism_button_calculate=findViewById(R.id.activity_calculate_metabolism_button_calculate);

        //Piechart
        pieChart=findViewById(R.id.activity_calculate_metabolism_piechart);

        //Metabolism
        activity_calculate_metabolism_height=findViewById(R.id.activity_calculate_metabolism_height);
        activity_calculate_metabolism_weight=findViewById(R.id.activity_calculate_metabolism_weight);
        activity_calculate_metabolism_age=findViewById(R.id.activity_calculate_metabolism_age);
        activity_calculate_metabolism_caloriestoconsume=findViewById(R.id.activity_calculate_metabolism_caloriestoconsume);
        activity_calculate_metabolism_save=findViewById(R.id.activity_calculate_metabolism_save);



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //
        String text=adapterView.getItemAtPosition(i).toString();
        switch (text) {
            case "Little to no exercise":
                nivelactivitate = 1;
                volumactivitate = 1.1f;

                break;
            case "Light Exercise (1-2 days/week)":
                nivelactivitate = 1;
                volumactivitate = 1.2f;

                break;
            case "Generally Active (3-5 days/week)":
                nivelactivitate = 1;
                volumactivitate = 1.355f;

                break;
            case "Active (5-7 days/week)":
                nivelactivitate = 1;
                volumactivitate = 1.55f;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    @SuppressLint("SetTextI18n")
    private void showPieChart(){

        pieChart.setVisibility(View.VISIBLE);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(R.color.Coral);
        pieChart.setCenterText("Values");
        pieChart.setCenterTextSize(12);
        pieChart.getDescription().setEnabled(false);
        pieChart.setNoDataText("Please Introduce Measurements");


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setTextColor(Color.WHITE);
        l.setDrawInside(false);
        l.setEnabled(true);
        float BMR;
        if(isMale){
            BMR=  Math.round((float)(66+(13.7*weight)+(5*height)-(6.8*age)));
        }
        else
        {
            BMR=  Math.round((float)(655+(9.6*weight)+(1.8*height)-(4.7*age)));
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        activity_calculate_metabolism_caloriestoconsume.setText("C: "+ (Math.round(BMR*volumactivitate)));
        float  pp= (float) ((0.25*BMR*volumactivitate)/4);
        float cc=(float)((0.5*BMR*volumactivitate)/4);
        float ff=(float)((0.25*BMR*volumactivitate)/9);
        float ss=(float)((0.13*BMR*volumactivitate)/4);

        entries.add(new PieEntry(pp, "Protein"));
        entries.add(new PieEntry(cc, "Carbs"));
        entries.add(new PieEntry(ff, "Fat"));
        entries.add(new PieEntry(ss, "Sugar"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.COLORFUL_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}