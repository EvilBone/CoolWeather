package com.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bone on 16/3/29.
 */
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    //Province建立语句
    private static final String CREATE_PROVINCE = "create table Province("
            + " id integer primary key autoincrement,"
            + " province_name text,"
            + " country_id integer)";
    //City建立语句
    private static final String CREATE_CITY = "create table City("
            + " id integer primary key autoincrement,"
            + " city_name text,"
            + " city_code text,"
            + " province_id integer)";
    //County建立语句
    private static final String CREATE_COUNTRY = "create table Country("
            + " id integer primary key autoincrement,"
            + " country_name text,"
            + " country_code text)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
