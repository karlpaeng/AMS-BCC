package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultView extends AppCompatActivity {
    private ArrayList<ScanModel> scanList;
    private RecyclerView recyclerView;
    TextView label;
    TextView query;
    Button export;

    int id_value;
    int year_value;
    String course_value;
    String section_value;
    String date_value;
    String name_value;

    boolean emptyResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        label = findViewById(R.id.labelTextView);
        query = findViewById(R.id.queryTextView);
        export = findViewById(R.id.export2file);
        recyclerView = findViewById(R.id.recView);

        DBHelper dbHalp = new DBHelper(ResultView.this);

        String prev = getIntent().getStringExtra("prev_act");
        if(prev.equals("act_id")){
            label.setText("ID/Student's name:");
            id_value = Integer.parseInt(getIntent().getStringExtra("id_value"));
            name_value = dbHalp.getStudentNameByID(id_value);

            scanList = dbHalp.searchScanByStudID(id_value);
            if(scanList.isEmpty()){
                query.setText("NO_RESULTS_FOUND");
            }else {
                query.setText(id_value + "/" + name_value);
            }

            RecAdapter adapter = new RecAdapter(scanList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

        }
        if(prev.equals("act_class")){
            label.setText("Class:");
            year_value = Integer.parseInt(getIntent().getStringExtra("yr_value"));
            course_value = getIntent().getStringExtra("course_value");
            section_value = getIntent().getStringExtra("section_value");

            scanList = dbHalp.searchScanByClass(""+year_value, course_value, section_value);
            query.setText(course_value + "-" + year_value + section_value);
            if(scanList.isEmpty()){
                query.setText(query.getText().toString() + "(NO_RESULTS_FOUND)");
            }

            RecAdapterByClass adapterByClass = new RecAdapterByClass(scanList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapterByClass);
        }
        if(prev.equals("act_date")){
            label.setText("Date:");
            date_value = getIntent().getStringExtra("date_value");

            scanList = dbHalp.searchScanByDate(date_value);
            query.setText(date_value);
            if(scanList.isEmpty()){
                query.setText(query.getText().toString() + "(NO_RESULTS_FOUND)");
            }

            RecAdapterByDate adapterByDate = new RecAdapterByDate(scanList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapterByDate);
        }
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scanList.isEmpty()){
                    Toast.makeText(ResultView.this, "There are no results to export", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}