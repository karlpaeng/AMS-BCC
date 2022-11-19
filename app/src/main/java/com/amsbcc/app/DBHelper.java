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
                "stud_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "stud_name TEXT, " +
                "stud_yr INTEGER, " +
                "stud_course TEXT, " +
                "stud_section TEXT, " +
                "stud_contact TEXT)"
        );
        sqLiteDatabase.execSQL("CREATE TABLE sign_in (" +
                "id INTEGER PRIMARY KEY, " +
                "last_sign TEXT, " +
                "status INTEGER, )"
        );
        addSigninRecord();
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

        long i = db.insert("sign_in", null, cv);

        db.close();
    }
    public void updateSigninRecord(int value){
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());
        db.execSQL("UPDATE sign_in SET last_sign = '" + date + "', status = " + value + " WHERE id = 1;");
        db.close();

    }
    public int getSigninStatus(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT status FROM sign_in WHERE id = 1;", null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}
