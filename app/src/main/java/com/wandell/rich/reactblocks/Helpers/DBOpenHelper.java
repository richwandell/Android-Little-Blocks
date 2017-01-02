package com.wandell.rich.reactblocks.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DBOpenHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "data.db";

    public static final String CREATE_SCORE_TABLE =
            "create table points (" +
                    "p_id INTEGER, " +
                    "points INTEGER, " +
                    "player_name TEXT, " +
                    "";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCORE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void debugAll(){
//        DBOpenHelper mDbHelper = new DBOpenHelper(RV.mainActivity);
//        SQLiteDatabase readableDatabase = mDbHelper.getReadableDatabase();
//        Cursor cursor = readableDatabase.query("scan_results", new String[]{
//                        "s_id", "fp_id", "ap_id", "coords", "value", "orig_values"},
//                null, null, null, null, null);
//        try {
//            cursor.moveToFirst();
//            do {
//                int s_id = cursor.getInt(0);
//                int fp_id = cursor.getInt(1);
//                String ap_id = cursor.getString(2);
//                String coords = cursor.getString(3);
//                double value = cursor.getDouble(4);
//                String orig_values = cursor.getString(5);
//                Log.d("rdebug", Integer.toString(s_id) + ", " +
//                        Integer.toString(fp_id) + ", " + ap_id + ", " + coords + ", " +
//                        Double.toString(value) + ", " + orig_values);
//
//                cursor.moveToNext();
//            } while (!cursor.isLast());
//        }catch(Exception e){
//            cursor.close();
//            mDbHelper.close();
//        }finally {
//            cursor.close();
//            mDbHelper.close();
//        }
    }
}
