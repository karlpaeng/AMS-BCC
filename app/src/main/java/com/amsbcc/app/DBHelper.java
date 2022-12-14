package com.amsbcc.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.apache.xmlbeans.impl.store.Cur;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "AMS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE scans (" +
                "scan_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "stud_num INTEGER, " +
                "name TEXT, " +
                "date TEXT, " +
                "time TEXT, " +
                "log TEXT)"
        );
        sqLiteDatabase.execSQL("CREATE TABLE students (" +
                "stud_id INTEGER PRIMARY KEY," +
                "stud_name TEXT, " +
                "stud_yr INTEGER, " +
                "stud_course TEXT, " +
                "stud_section TEXT, " +
                "stud_contact TEXT)"
        );
        sqLiteDatabase.execSQL("CREATE TABLE signin (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "last_sign TEXT, " +
                "status INTEGER)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void addSigninRecord(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());
        cv.put("id", 1);
        cv.put("last_sign", date);
        cv.put("status", 0);

        long i = db.insert("signin", null, cv);

        db.close();
    }
    public void updateSigninRecord(int value){
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());
        db.execSQL("UPDATE signin SET status = " + value + " WHERE id = 1;");
        if(value != 0){
            db.execSQL("UPDATE signin SET last_sign = '" + date + "' WHERE id = 1;");
        }
        db.close();

    }
    public int getSigninStatus(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT status FROM signin WHERE id = 1;", null);
        cursor.moveToFirst();
        int anInt = cursor.getInt(0);
        cursor.close();
        return anInt;
    }
    public boolean checkExistingSignin(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT status FROM signin WHERE id = 1;", null);
        return cursor.moveToFirst();
    }
    public String getLastSign(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_sign FROM signin WHERE id = 1;", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
    public void clearStudentTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM students");
    }
    public long inputStudentToDB(StudentModel student){
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM students");
        ContentValues cv = new ContentValues();

        //cv.put("id", 1);
        cv.put("stud_id", student.studID);
        cv.put("stud_name", student.studName);
        cv.put("stud_yr", student.studYear);
        cv.put("stud_course", student.studCourse);
        cv.put("stud_section", student.studSection);
        cv.put("stud_contact", student.getStudContNum());

        long i = db.insert("students", null, cv);

        return i;

    }
    public void closeDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }
    public StudentModel searchStudentByID(int id){
        StudentModel student;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE stud_id = " + id + ";", null);
        if(cursor.moveToFirst()) {

            student = new StudentModel(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getLong(5)
            );

        }else{
            student = new StudentModel(-1, "-", "-", "-", "-", -1);
        }
        cursor.close();
        return student;
    }
    public long inputScanToDB(ScanModel scan){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        //cv.put("id", 1);
        cv.put("stud_num", scan.studentID);
        cv.put("name", scan.name);
        cv.put("date", scan.date);
        cv.put("time", scan.time);
        cv.put("log", scan.log);

        long i = db.insert("scans", null, cv);

        db.close();
        return i;
    }
    public void clearScanTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM scans");
    }
    public ArrayList<ScanModel> searchScanByStudID(int studID){
        ArrayList<ScanModel> returnList = new ArrayList<>();
        //String queryString = "SELECT * FROM scans WHERE stud_num = " + studID + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scans WHERE stud_num = " + studID + ";", null);
        if (cursor.moveToFirst()) {
            ScanModel scanHead = new ScanModel(-1, "NAME", "DATE", "TIME", "LOG");
            returnList.add(scanHead);
            do {
                int StudID = cursor.getInt(1);
                String name = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String log = cursor.getString(5);

                ScanModel scan = new ScanModel(StudID, name, date, time, log);
                returnList.add(scan);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }

    public ArrayList<ScanModel> searchScanByClass(String year, String course, String section){
        ArrayList<ScanModel> returnList = new ArrayList<>();
        //String queryString = "SELECT * FROM scans WHERE stud_num = " + studID + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scans WHERE stud_num IN (SELECT stud_id FROM students WHERE " +
                "stud_yr = " + year + " AND " +
                "stud_course = '" + course + "' AND " +
                "stud_section = '" + section +"');", null);
        if (cursor.moveToFirst()) {
            ScanModel scanHead = new ScanModel(-1, "NAME", "DATE", "TIME", "LOG");
            returnList.add(scanHead);
            do {
                int StudID = cursor.getInt(1);
                String name = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String log = cursor.getString(5);

                ScanModel scan = new ScanModel(StudID, name, date, time, log);
                returnList.add(scan);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }
    public ArrayList<ScanModel> searchScanByDate(String dateX){
        ArrayList<ScanModel> returnList = new ArrayList<>();
        //String queryString = "SELECT * FROM scans WHERE stud_num = " + studID + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scans WHERE date = '" + dateX + "';", null);
        if (cursor.moveToFirst()) {
            ScanModel scanHead = new ScanModel(-1, "NAME", "DATE", "TIME", "LOG");
            returnList.add(scanHead);
            do {
                int StudID = cursor.getInt(1);
                String name = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String log = cursor.getString(5);

                ScanModel scan = new ScanModel(StudID, name, date, time, log);
                returnList.add(scan);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }
    public ArrayList<ScanModel> allScanRecords(){
        ArrayList<ScanModel> returnList = new ArrayList<>();
        //String queryString = "SELECT * FROM scans WHERE stud_num = " + studID + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM scans", null);
        if (cursor.moveToFirst()) {
            do {
                int StudID = cursor.getInt(1);
                String name = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                String log = cursor.getString(5);

                ScanModel scan = new ScanModel(StudID, name, date, time, log);
                returnList.add(scan);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }
    public String getStudentNameByID(int q){
        String returnStr;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE stud_id = " + q + ";", null);
        if (cursor.moveToFirst()) {
            returnStr = cursor.getString(1);
        } else {
            returnStr = "NO_RESULT";
        }

        cursor.close();
        db.close();
        return returnStr;
    }
}
