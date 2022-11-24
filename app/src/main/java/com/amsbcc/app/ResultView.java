package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultView extends AppCompatActivity {
    TextView label;
    TextView query;
    Button export;

    int id_value;
    int year_value;
    String course_value;
    String section_value;
    String date_value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        label = findViewById(R.id.labelTextView);
        query = findViewById(R.id.queryTextView);
        export = findViewById(R.id.export2file);

        String prev = getIntent().getStringExtra("prev_act");
        if(prev.equals("act_id")){
            label.setText("ID:");
            id_value = Integer.parseInt(getIntent().getStringExtra("id_value"));
            query.setText(""+id_value);
        }
        if(prev.equals("act_class")){

        }
        if(prev.equals("act_date")){
            label.setText("Date:");
            date_value = getIntent().getStringExtra("date_value");
            query.setText(date_value);
        }
    }
}