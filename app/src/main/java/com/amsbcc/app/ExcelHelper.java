package com.amsbcc.app;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ExcelHelper {
    public ExcelHelper(){}

    public String saveToFile(XSSFWorkbook xwb, String fileName, Context context)throws IOException {
        OutputStream fileOutputStream;
        //File filePath;
        String retStr;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentValues values = new ContentValues();

            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);       //file name
            values.put(MediaStore.MediaColumns.MIME_TYPE, "document/xlsx");        //file extension, will automatically add to file
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/BCC AMS/");     //end "/" is not mandatory

            Uri uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);      //important!

            fileOutputStream = context.getContentResolver().openOutputStream(uri);
            retStr = Environment.DIRECTORY_DOCUMENTS + "/" + fileName;

        }else{
            String docsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
            File file = new File(docsDir, fileName);
            fileOutputStream = new FileOutputStream(file);

//            filePath = new File(getContext().getExternalFilesDir(null) + "/" + fileName);;
//            if(filePath.exists()) filePath.createNewFile();
//            else filePath = new File(getContext().getExternalFilesDir(null) + "/" + fileName);
//            fileOutputStream = new FileOutputStream(filePath);
            retStr = docsDir + "/" + fileName;

        }
        try {
            xwb.write(fileOutputStream);
            fileOutputStream.flush();

        } catch (Exception e) {

            e.printStackTrace();

        }finally {
            fileOutputStream.close();
        }
        return retStr;
    }
    public void insertToDB(Context context, Uri uri) throws Exception {
        DBHelper dbHalp = new DBHelper(context);

        InputStream inStream;
        inStream = context.getContentResolver().openInputStream(uri);

        Workbook wb = new XSSFWorkbook(inStream);
        Sheet sh = wb.getSheetAt(0);
        int rowNum = sh.getLastRowNum();

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
        Toast.makeText(context, "File imported with " + rowNum + " rows of data", Toast.LENGTH_SHORT).show();
    }
    public XSSFWorkbook saveToWorkbook(ArrayList<ScanDisplayModel> scanList){
        XSSFWorkbook xwb = new XSSFWorkbook();
        XSSFSheet xsheet = xwb.createSheet("AMS BCC Data");
        xsheet.setColumnWidth(1, 20 * 256);
        xsheet.setColumnWidth(2, 15 * 256);
        xsheet.setColumnWidth(3, 15 * 256);
        xsheet.setColumnWidth(4, 15 * 256);
        int listSize = scanList.size();
        for (int q = 1 ; q < listSize ; q++){
            XSSFRow xRow = xsheet.createRow(q-1);
            XSSFCell xCell = xRow.createCell(0);

            xCell.setCellValue(scanList.get(q).studentID + "");
            xCell = xRow.createCell(1);
            xCell.setCellValue(scanList.get(q).name);
            xCell = xRow.createCell(2);
            xCell.setCellValue(scanList.get(q).date);
            xCell = xRow.createCell(3);
            xCell.setCellValue(scanList.get(q).timeIn);
            xCell = xRow.createCell(4);
            xCell.setCellValue(scanList.get(q).timeOut);
        }
        return xwb;
    }
}
