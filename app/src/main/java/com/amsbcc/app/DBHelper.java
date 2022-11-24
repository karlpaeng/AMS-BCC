package com.amsbcc.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    ///private Database dbHelper;
    public DBHelper(@Nullable Context context) {
        super(context, "AMS.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE scans (" +
                "scan_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "scan_stud_num INTEGER, " +
                "scan_stud_name TEXT, " +
                "scan_date TEXT, " +
                "scan_time TEXT, " +
                "scan_log TEXT)"
        );
        sqLiteDatabase.execSQL("CREATE TABLE students (" +
                "stud_id INTEGER PRIMARY KEY AUTOINCREMENT," +
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
    public int insert(String table, ContentValues values) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int y = (int) db.insert(table, null, values);
            db.close();
            Log.e("Data Inserted", "Data Inserted");
            Log.e("y", y + "");
            return y;
        } catch (Exception ex) {
            Log.e("Error Insert", ex.getMessage().toString());
            return 0;
        }
    }
    public void open() {
        //SQLiteDatabase db;
        if (null == db || !db.isOpen()) {
            try {
                db = this.getWritableDatabase();
            } catch (SQLiteException sqLiteException) {
            }
        }
    }
    public void delete(String tablename) {
        db.execSQL("delete from " + tablename);
    }
}
