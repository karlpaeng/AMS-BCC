package com.amsbcc.app;

import android.content.ContentValues;
import android.inputmethodservice.Keyboard;
import android.util.Log;

import com.amsbcc.app.DBHelper;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;
//
public class ExcelHelper {

    public static final String StudentTable = "students";
    public static final String ScanTable = "scans";

    public static final String Stud_id = "stud_id";// 0 integer
    public static final String Stud_Name = "stud_name";// 1 text(String)
    public static final String Stud_Year = "stud_year";// 2 integer
    public static final String Stud_Course = "stud_course";// 3 date(String)
    public static final String Stud_Section = "stud_section";// 4 date(String)
    public static final String Stud_Contact = "stud_contact";// 5 date(String)


    public static final String Scan_id = "scan_id";
    public static final String Scan_stud_num = "scan_stud_num";
    public static final String Scan_stud_name = "scan_stud_name";
    public static final String Scan_date = "scan_date";
    public static final String Scan_time = "scan_time";
    public static final String Scan_log = "scan_log";

    public static void insertExcelToSqlite(DBHelper dbAdapter, Sheet sheet) {

        for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext(); ) {
            Row row = rit.next();

            ContentValues contentValues = new ContentValues();
            row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);

            contentValues.put(Stud_id, row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Stud_Name, row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Stud_Year, row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Stud_Course, row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Stud_Section, row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
            contentValues.put(Stud_Contact, row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());

            try {
                if (dbAdapter.insert(StudentTable, contentValues) < 0) {
                    return;
                }
            } catch (Exception ex) {
                Log.d("Exception in importing", ex.getMessage().toString());
            }
        }
    }
}