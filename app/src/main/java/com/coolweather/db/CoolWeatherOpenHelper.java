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

    //天气信息
    private static final String CREATE_WEATHERINFO = "create table weatherinfo("
            +"id integer primary key autoincrement,"
            +"city_code text,"
            +"city_name text,"
            +"city_select int,"
            +"update_loc text,"
            +"update_utc text)";
    private static final String CREATE_WEATHERNOW = "create table weathernow("
            +"id integer primary key autoincrement,"
            +"city_code text,"
            +"weather_id int,"
            +"cond_code text,"
            +"cond_txt text,"
            +"weather_fl text,"
            +"weather_spd text,"
            +"weather_sc text,"
            +"weather_deg text,"
            +"weather_dir text,"
            +"weather_pcpn text,"
            +"weather_tmp text,"
            +"weather_hum text,"
            +"weather_pres text,"
            +"weather_vis text)";
    private static final String CREATE_WEATHERDAILY = "create table dailyforecast("
            +"id integer primary key autoincrement,"
            +"city_code text,"
            +"weather_date text,"
            +"astro_sr text,"
            +"astro_ss text,"
            +"cond_code_d text,"
            +"cond_code_n text,"
            +"cond_txt_d text,"
            +"cond_txt_n text,"
            +"weather_hum text,"
            +"weather_pcpn text,"
            +"weather_pop text,"
            +"weather_pres text,"
            +"tmp_max text,"
            +"tmp_min text,"
            +"weather_vis text,"
            +"wind_seg text,"
            +"wind_dir text,"
            +"wind_sc text,"
            +"wind_spd text)";



    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTRY);
        db.execSQL(CREATE_WEATHERINFO);
        db.execSQL(CREATE_WEATHERNOW);
        db.execSQL(CREATE_WEATHERDAILY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
