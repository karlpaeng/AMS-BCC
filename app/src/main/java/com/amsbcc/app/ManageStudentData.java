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

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
                tag = "export";
                alertDia("Export all Scan records?","This will create a file containing ALL Scan records currently saved in this device. The XLSX file will be saved to your local storage at Android/data/com.amsbcc.app/files/");

                //Toast.makeText(ManageStudentData.this, filePath, Toast.LENGTH_LONG).show();
                //Log.d("asd:", filePath);
                //Log.d("asd:", uriString);

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag = "clear";
                alertDia("Clear all Scan records?","This will delete all Scan records stored in this device. Make sure to export the records first to avoid data loss.");

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
                    ArrayList<ScanModel> scanList = dbHalp.allScanRecords();
                    XSSFWorkbook xwb = new XSSFWorkbook();
                    XSSFSheet xsheet = xwb.createSheet("AMS BCC Data");
                    xsheet.setColumnWidth(1, 20 * 256);
                    xsheet.setColumnWidth(2, 15 * 256);
                    xsheet.setColumnWidth(3, 15 * 256);
                    int listSize = scanList.size();
                    for (int q = 0 ; q < listSize ; q++){
                        XSSFRow xRow = xsheet.createRow(q);
                        XSSFCell xCell = xRow.createCell(0);
                        xCell.setCellValue(scanList.get(q).studentID + "");
                        xCell = xRow.createCell(1);
                        xCell.setCellValue(scanList.get(q).name);
                        xCell = xRow.createCell(2);
                        xCell.setCellValue(scanList.get(q).date);
                        xCell = xRow.createCell(3);
                        xCell.setCellValue(scanList.get(q).time);
                        xCell = xRow.createCell(4);
                        xCell.setCellValue(scanList.get(q).log);
                    }
//                    XSSFRow xRow = xsheet.createRow(0);
//                    XSSFCell xCell = xRow.createCell(0);
//                    xCell.setCellValue("asd");

                    String dateNow = new SimpleDateFormat("yyyyMMMdd-hhmmssa", Locale.getDefault()).format(new Date());

                    File filePath = new File(ManageStudentData.this.getExternalFilesDir(null) + "/amsbcc-" + dateNow + "-exported.xlsx");;
                    try {
                        if(filePath.exists()) filePath.createNewFile();
                        else filePath = new File(ManageStudentData.this.getExternalFilesDir(null) + "/amsbcc-" + dateNow + "-exported.xlsx");
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        xwb.write(fileOutputStream);
                        if (fileOutputStream!=null){
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        Toast.makeText(ManageStudentData.this, "File was created:" + filePath.toString(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(ManageStudentData.this, "File failed to create", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d("asd:", e.toString());
                    }
                }else{
                    dialogInterface.dismiss();
                }
            }
        }).show();
    }

}