package com.example.meplusplus.CalorieCalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.meplusplus.R;

import org.w3c.dom.Text;

import java.util.Locale;

public class CalculateMetabolismActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Controale
    RadioGroup activity_calculate_metabolism_radiogroup;
    RadioButton activity_calculate_metabolism_option_male;
    RadioButton activity_calculate_metabolism_option_female;
    Spinner activity_calculate_metabolism_spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_metabolism);
        init();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(CalculateMetabolismActivity.this,R.array.numbers, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);


        activity_calculate_metabolism_spinner.setAdapter(adapter);
        activity_calculate_metabolism_spinner.setOnItemSelectedListener(CalculateMetabolismActivity.this);

        activity_calculate_metabolism_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (i){
                    case R.id.activity_calculate_metabolism_option_male:
                        Toast.makeText(CalculateMetabolismActivity.this, "Male Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.activity_calculate_metabolism_option_female:
                        Toast.makeText(CalculateMetabolismActivity.this, "Female Selected", Toast.LENGTH_SHORT).show();
                        break;

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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text=adapterView.getItemAtPosition(i).toString();
        if(!text.equals(""))
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}