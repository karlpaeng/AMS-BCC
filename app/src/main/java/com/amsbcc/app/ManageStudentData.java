package com.amsbcc.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class ManageStudentData extends AppCompatActivity {
    /*static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }*/
    private static final int PERMISSION_REQUEST_MEMORY_ACCESS = 0;
    private static String fileType = "XLSX";
    private View mLayout;
    private static String extensionXLS = "XLS";
    private static String extensionXLXS = "XLSX";
    ActivityResultLauncher<Intent> filePicker;

    Button update;
    Button export;
    Button clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_student_data);
        update = findViewById(R.id.updateStudentListBtn);
        export = findViewById(R.id.exportAllBtn);
        clear = findViewById(R.id.clrAllBtn);
        update = findViewById(R.id.updateStudentListBtn);
        mLayout = findViewById(R.id.main_layout);

        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent intent1 = result.getData();

                        Uri uri = intent1.getData();
                        ReadExcelFile(ManageStudentData.this
                                , uri);

                    }
                });
    }
    private boolean CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            Snackbar.make(mLayout, R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestStoragePermission();
                }
            }).show();


            return false;
        }
    }
    public void ReadExcelFile(Context context, Uri uri) {
        try {
            InputStream inStream;
            Workbook wb = null;

            try {
                inStream = context.getContentResolver().openInputStream(uri);

                if (fileType == extensionXLS)
                    wb = new HSSFWorkbook(inStream);
                else
                    wb = new XSSFWorkbook(inStream);

                inStream.close();
            } catch (IOException e) {
                //lbl.setText("First " + e.getMessage().toString());
                e.printStackTrace();
            }

            DBHelper dbAdapter = new DBHelper(this);
            Sheet sheet1 = wb.getSheetAt(0);

            dbAdapter.open();
            dbAdapter.delete("students");
            dbAdapter.close();
            dbAdapter.open();
            ExcelHelper.insertExcelToSqlite(dbAdapter, sheet1);

            dbAdapter.close();

            //FillList();
        } catch (Exception ex) {
            Toast("ReadExcelFile Error:" + ex.getMessage().toString(), ex);
        }
    }


    public void ChooseFile() {
        try {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.addCategory(Intent.CATEGORY_OPENABLE);

            if (fileType == extensionXLS)
                fileIntent.setType("application/vnd.ms-excel");
            else
                fileIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            filePicker.launch(fileIntent);
        } catch (Exception ex) {
            Toast("ChooseFile error: " + ex.getMessage().toString(), ex);

        }
    }

    void Toast(String message, Exception ex) {
        if (ex != null)
            Log.e("Error", ex.getMessage().toString());
        Toast.makeText(ManageStudentData.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_MEMORY_ACCESS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                OpenFilePicker(mLayout);
            } else {
                Snackbar.make(mLayout, R.string.storage_access_denied,
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void requestStoragePermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(ManageStudentData.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_MEMORY_ACCESS);

        } else {
            Snackbar.make(mLayout, R.string.storage_unavailable, Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_MEMORY_ACCESS);
        }
    }
    public void OpenFilePicker(View v) {
        try {
            if (CheckPermission()) {
                ChooseFile();
            }
        } catch (ActivityNotFoundException e) {
            //lbl.setText("No activity can handle picking a file. Showing alternatives.");
        }

    }
}