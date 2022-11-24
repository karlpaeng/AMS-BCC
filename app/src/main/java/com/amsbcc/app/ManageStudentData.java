package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ManageStudentData extends AppCompatActivity {
    Button update;
    Button export;
    Button clear;
    //, uriString;
    //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student_data);
        update = findViewById(R.id.updateStudentListBtn);
        export = findViewById(R.id.exportAllBtn);
        clear = findViewById(R.id.clrAllBtn);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ManageStudentData.this, SelectFile.class);
                startActivity(intent);

            }
        });
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//test code
                //Toast.makeText(ManageStudentData.this, filePath, Toast.LENGTH_LONG).show();
                //Log.d("asd:", filePath);
                //Log.d("asd:", uriString);
            }
        });
    }

}