package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewRecords extends AppCompatActivity {
    Button searchID;
    Button searchClass;
    Button searchDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);

        searchID = (Button) findViewById(R.id.searchIdBtn);
        searchClass = (Button) findViewById(R.id.searchClassBtn);
        searchDate = (Button) findViewById(R.id.searchDateBtn);

        searchID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRecords.this, SearchID.class);
                startActivity(intent);
            }
        });
        //searchClass
        searchClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRecords.this, SearchClass.class);
                startActivity(intent);
            }
        });
        //searchDate
        searchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRecords.this, SearchDate.class);
                startActivity(intent);
            }
        });

    }
}