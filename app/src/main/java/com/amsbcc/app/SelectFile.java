package com.amsbcc.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

public class SelectFile extends AppCompatActivity {
    Button selectFile;
    TextView pathTV;
    Button importFile;

    Uri studListUri;
    String filePath;
    Bundle bundle;

    private static final int CODE = 1001;

    DBHelper dbHalp = new DBHelper(SelectFile.this);
    ExcelHelper xcHalp = new ExcelHelper();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        setContentView(R.layout.activity_select_file);
        selectFile = findViewById(R.id.fileSelectBtn);
        pathTV = findViewById(R.id.pathTextView);
        importFile = findViewById(R.id.importBtn);

        filePath = "";

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                startActivityForResult(intent, CODE);
                //pathTV.setText();
            }
        });
        importFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath.equals("")){
                    Toast.makeText(SelectFile.this, "Pls select an xlsx file first", Toast.LENGTH_SHORT).show();
                }else{
                    //
                    //Toast.makeText(SelectFile.this, "selected", Toast.LENGTH_SHORT).show();
                    try {
                        xcHalp.insertToDB(SelectFile.this, studListUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SelectFile.this, "Exception:"+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //
        if (data == null){
            Intent intent = new Intent(SelectFile.this, SelectFile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return;
        }
        studListUri = data.getData();

        //Log.d("asd: ", filePath + "q");
        File tempFile = new File(studListUri.getPath());
        final String[] split = tempFile.getPath().split(":");//split the path.
        filePath = split[1];
        //filePath = tempFile.getAbsolutePath();

        //filePath = data.getDataString();
        pathTV.setText(filePath);



        super.onActivityResult(requestCode, resultCode, data);
    }


}