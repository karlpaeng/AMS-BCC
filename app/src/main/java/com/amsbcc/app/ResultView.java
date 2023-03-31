package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ResultView extends AppCompatActivity {
    private ArrayList<ScanDisplayModel> scanList;
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
    String fileNameAdd;

    ExcelHelper xcHalp = new ExcelHelper();
    DBHelper dbHalp = new DBHelper(ResultView.this);

    //boolean emptyResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        label = findViewById(R.id.labelTextView);
        query = findViewById(R.id.queryTextView);
        export = findViewById(R.id.export2file);
        recyclerView = findViewById(R.id.recView);

        int permission = ActivityCompat.checkSelfPermission(ResultView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    ResultView.this,
                    new String[]{

                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PackageManager.PERMISSION_GRANTED
            );
        }
        String prev = getIntent().getStringExtra("prev_act");
        if(prev.equals("act_id")){
            label.setText("(ID)Student's name:");
            id_value = Integer.parseInt(getIntent().getStringExtra("id_value"));
            name_value = dbHalp.getStudentNameByID(id_value);

            scanList = dbHalp.searchScanByStudID(id_value);
            if(scanList.isEmpty()){
                query.setText("NO_RESULTS_FOUND");
            }else {
                query.setText("(" + id_value + ")" + name_value);
                fileNameAdd = "-searched-" + id_value;
            }

        }
        if(prev.equals("act_class")){
            label.setText("Class:");
            year_value = Integer.parseInt(getIntent().getStringExtra("yr_value"));
            course_value = getIntent().getStringExtra("course_value");
            section_value = getIntent().getStringExtra("section_value");

            scanList = dbHalp.searchScanByClass(""+year_value, course_value, section_value);
            query.setText(course_value + "-" + year_value + section_value);
            fileNameAdd = "-searched-" + course_value + year_value + section_value;
            if(scanList.isEmpty()){
                query.setText(query.getText().toString() + "(NO_RESULTS_FOUND)");
            }

        }
        if(prev.equals("act_date")){
            label.setText("Date:");
            date_value = getIntent().getStringExtra("date_value");
            fileNameAdd = "-searched-" + date_value;

            scanList = dbHalp.searchScanByDate(date_value);
            query.setText(date_value);
            if(scanList.isEmpty()){
                query.setText(query.getText().toString() + "(NO_RESULTS_FOUND)");
            }

        }
        RecAdapterDashB adapter = new RecAdapterDashB(scanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scanList.isEmpty()){
                    Toast.makeText(ResultView.this, "There are no results to export", Toast.LENGTH_SHORT).show();
                }else{
                    //
                    XSSFWorkbook xwb = xcHalp.saveToWorkbook(scanList);

                    String dateNow = new SimpleDateFormat("yyyyMMMdd-hhmmssa", Locale.getDefault()).format(new Date());
                    String fileName = "amsbcc-" + dateNow + fileNameAdd + ".xlsx";
                    String path;
                    try {
                        path = xcHalp.saveToFile(xwb, fileName, ResultView.this);
                        Toast.makeText(ResultView.this, "File was created:" + path, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ResultView.this, "File failed to create", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d("asd:", e.toString());
                    }
                }
            }
        });
    }
}