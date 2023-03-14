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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Text;

import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SelectFile extends AppCompatActivity {
    private static Workbook wb;
    private static Sheet sh;
    private static Row row;
    private static Cell cell;

    Button selectFile;
    TextView pathTV;
    Button importFile;

    Uri studListUri;
    String filePath;
    Bundle bundle;

    private static final int CODE = 1001;

    DBHelper dbHalp;


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
                        insertToDB(SelectFile.this, studListUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(SelectFile.this, "FNF Exception", Toast.LENGTH_SHORT).show();
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
    private void insertToDB(Context context, Uri uri) throws FileNotFoundException {
        dbHalp = new DBHelper(SelectFile.this);
        InputStream inStream;
//
        inStream = context.getContentResolver().openInputStream(uri);
        try {
            wb = new XSSFWorkbook(inStream);
        } catch (IOException e) {
            Toast.makeText(SelectFile.this, "IOException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        sh = wb.getSheetAt(0);
        int rowNum = sh.getLastRowNum();

        //Log.d("asd:", ""+rowNum);
        //long x = 0;
        dbHalp.clearStudentTable();
        for (int q = 1 ; q <= rowNum ; q++){
            StudentModel student = new StudentModel(
                    (int) Double.parseDouble(sh.getRow(q).getCell(0).toString()),
                    sh.getRow(q).getCell(1).toString(),
                    sh.getRow(q).getCell(2).toString(),
                    sh.getRow(q).getCell(3).toString(),
                    sh.getRow(q).getCell(4).toString(),
                    Long.parseLong(sh.getRow(q).getCell(5).toString())

            );
            long lInput = dbHalp.inputStudentToDB(student);
            //x+=lInput;

        }
        dbHalp.closeDB();
        Toast.makeText(SelectFile.this, "File imported with " + rowNum + " rows of data", Toast.LENGTH_SHORT).show();


    }

}