package com.amsbcc.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    String tag = "";
    DBHelper dbHalp;


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
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDia("Clear all Scan records?","This will delete all Scan records stored in this device. Make sure to export the records first to avoid data loss.");
                tag = "clear";
            }
        });
    }
    private void alertDia(String buildTitle, String buildMessage){
        dbHalp = new DBHelper(ManageStudentData.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageStudentData.this);
        builder.setTitle(buildTitle);
        builder.setMessage(buildMessage);
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbHalp = new DBHelper(ManageStudentData.this);
                if (tag.equals("clear")){
                    dbHalp.clearScanTable();
                    dbHalp.closeDB();
                    Toast.makeText(ManageStudentData.this, "Scan records cleared", Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }else if (tag.equals("export")){
                    //do export here
                }else{
                    dialogInterface.dismiss();
                }
            }
        }).show();
    }

}