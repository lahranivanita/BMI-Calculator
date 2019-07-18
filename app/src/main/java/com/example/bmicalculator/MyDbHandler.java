package com.example.bmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


/**
 * Created by HP on 05-07-2018.
 */

public class MyDbHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;
    Context context;
    MyDbHandler(Context context) {
        super(context, "bmidb", null, 1);
        this.context = context;
        db = this.getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table history (d varchar," +"wtkg varchar," + "bmi varchar," +"show varchar)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
    public void add(String d, double wtkg , double bmi,String show) {

        ContentValues cv = new ContentValues();
        cv.put("d", d);
        cv.put("wtkg", wtkg);
        cv.put("bmi",bmi);
        cv.put("show",show);
        long rid = db.insert("history", null, cv);

        if (rid < 0)
            Toast.makeText(context, "Issue :(", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Inserted Successfully", Toast.LENGTH_SHORT).show();

    }
    public String viewhistory() {
        Cursor cursor = db.query("history", null, null, null, null, null, null, null);
        StringBuffer sb = new StringBuffer();
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            do {
                sb.append("Date : " + cursor.getString(0) + "\n" + "Weight : " + cursor.getString(1) + "\n" +
                        "BMI :" + cursor.getString(2) + " " + cursor.getString(3) +
                        "\n" + "------------------------------------" + "\n" );
            } while (cursor.moveToNext());
        }

        return sb.toString();
    }

}
