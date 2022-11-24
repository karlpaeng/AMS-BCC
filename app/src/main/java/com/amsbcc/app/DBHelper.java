package com.amsbcc.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        return cursor.getInt(0);
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
}
