package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Locale;

public class SearchClass extends AppCompatActivity {
    EditText yr, course, section;
    Button search;

    String yrStr, courseStr, sectionStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_class);
        yr = findViewById(R.id.year);
        course = findViewById(R.id.Course);
        section = findViewById(R.id.section);
        search = findViewById(R.id.searchClassBtn);



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseStr = course.getText().toString().toUpperCase();
                sectionStr = section.getText().toString().toUpperCase();
                yrStr = yr.getText().toString();
                if(false) {//sanitize using regex
                }
                else{
                    Intent intent = new Intent(SearchClass.this, ResultView.class);
                    intent.putExtra("prev_act", "act_class");
                    intent.putExtra("yr_value", yrStr);
                    intent.putExtra("course_value", courseStr);
                    intent.putExtra("section_value", sectionStr);
                    startActivity(intent);
                }



            }
        });
    }

}