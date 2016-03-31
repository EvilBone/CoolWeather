package com.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import com.coolweather.db.CoolWeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bone on 16/3/29.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    //私有化构造函数？
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHepler = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHepler.getWritableDatabase();
    }

    //获取CoolWeatherDB实例
    public synchronized static CoolWeatherDB getInstance(Context context) {
        if (coolWeatherDB == null) {
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //存储Province实例到数据库
    public Province saveProvince(Province province) {
        if (province != null) {
            String query = "SELECT id from Province order by id DESC limit 1";
            ContentValues values = new ContentValues();
            values.put("province_name", province.getProvinceName());
            values.put("country_id", province.getCountryId());
            db.insert("Province", null, values);
            Cursor c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                province.setCountryId(c.getInt(0));
            }
        }
        return province;
    }


    //获取所有省份
    public List<Province> loadProvinces(int countryId) {
        List<Province> provinces = new ArrayList<Province>();
        Cursor cursor = db.query("Province", null, "country_id = ?", new String[]{String.valueOf(countryId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setCountryId(cursor.getInt(cursor.getColumnIndex("country_id")));
                provinces.add(province);
            } while (cursor.moveToNext());
        }
        return provinces;
    }

    public Province loadProvince(String provinceName) {
        Province province = null;
        Cursor cursor = db.query("Province", null, "province_name = ?", new String[]{provinceName}, null, null, null);
        if (cursor.moveToFirst()) {
            province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setCountryId(cursor.getInt(cursor.getColumnIndex("country_id")));
        }
        return province;
    }

    public Province loadProvince(int provinceId) {
        Province province = null;
        Cursor cursor = db.query("Province", null, "id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            province = new Province();
            province.setId(cursor.getInt(cursor.getColumnIndex("id")));
            province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
            province.setCountryId(cursor.getInt(cursor.getColumnIndex("country_id")));
        }
        return province;
    }

    //存储City实例到数据库
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();
            values.put("city_name", city.getCityName());
            values.put("city_code", city.getCityCode());
            values.put("province_id", city.getProvinceId());
            db.insert("City", null, values);
        }
    }

    //获取某省的所有城市
    public List<City> loadCities(int provinceId) {
        List<City> cities = new ArrayList<City>();
        Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                cities.add(city);
            } while (cursor.moveToNext());
        }
        return cities;
    }

    //    存储Country实例数据库
    public Country saveCountry(Country country) {
        if (country != null) {
            String query = "SELECT id from Country order by id DESC limit 1";
            ContentValues values = new ContentValues();
            values.put("country_name", country.getCountryName());
            db.insert("Country", null, values);
            Cursor c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                country.setId(c.getInt(0));
            }
        }
        return country;
    }

    //    获取所有国家
    public List<Country> loadCountries() {
        List<Country> counties = new ArrayList<Country>();
        Cursor cursor = db.query("Country", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                counties.add(country);
            } while (cursor.moveToNext());
        }
        return counties;
    }

    public Country loadCountry(String countryName) {
        Cursor cursor = db.query("Country", null, "country_name = ?", new String[]{countryName}, null, null, null);
        Country country = null;
        if (cursor.moveToFirst()) {
            country = new Country();
            country.setId(cursor.getInt(cursor.getColumnIndex("id")));
            country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
        }
        return country;
    }

    public Country loadCountry(int countryId) {
        Cursor cursor = db.query("Country", null, "id = ?", new String[]{String.valueOf(countryId)}, null, null, null);
        Country country = null;
        if (cursor.moveToFirst()) {
            country = new Country();
            country.setId(cursor.getInt(cursor.getColumnIndex("id")));
            country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
        }
        return country;
    }

    public List<WeatherInfo> loadWeatherInfos(){
        List<WeatherInfo> infoList = new ArrayList<>();
        Cursor cursor = db.query("weatherinfo",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                WeatherInfo weatherInfo = new WeatherInfo();
                String cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                weatherInfo.setId(cityCode);
                weatherInfo.setCity(cursor.getString(cursor.getColumnIndex("city_name")));
                int sel = cursor.getInt(cursor.getColumnIndex("city_select"));
                boolean isSelect = false;
                if(sel>0)
                    isSelect = true;
                weatherInfo.setCitySelect(isSelect);
                weatherInfo.setUpdateLoc(cursor.getString(cursor.getColumnIndex("update_loc")));
                weatherInfo.setUpdateUtc(cursor.getString(cursor.getColumnIndex("update_utc")));
                WeatherNow weatherNow = loadWeatherNow(cityCode);
                weatherInfo.setWeatherNow(weatherNow);
                List<WeatherDailyForecast> forecast = loadDailyForecast(cityCode);
                weatherInfo.setWeatherDailyForecast(forecast);
                infoList.add(weatherInfo);
            }while (cursor.moveToNext());
        }
        return infoList;
    }

    public WeatherNow loadWeatherNow(String cityCode){
        WeatherNow weatherNow = new WeatherNow();
        Cursor cursor = db.query("weathernow",null,"city_code = ?",new String[]{cityCode},null,null,null);
        if (cursor.moveToFirst()){
            weatherNow.setTmp(cursor.getString(cursor.getColumnIndex("weather_tmp")));
            weatherNow.setCode(cursor.getString(cursor.getColumnIndex("cond_code")));
            weatherNow.setTxt(cursor.getString(cursor.getColumnIndex("cond_txt")));
            weatherNow.setSc(cursor.getString(cursor.getColumnIndex("weather_sc")));
            weatherNow.setPres(cursor.getString(cursor.getColumnIndex("weather_pres")));
            weatherNow.setVis(cursor.getString(cursor.getColumnIndex("weather_vis")));
            weatherNow.setDeg(cursor.getString(cursor.getColumnIndex("weather_deg")));
            weatherNow.setFl(cursor.getString(cursor.getColumnIndex("weather_fl")));
            weatherNow.setDir(cursor.getString(cursor.getColumnIndex("weather_dir")));
            weatherNow.setPcpn(cursor.getString(cursor.getColumnIndex("weather_pcpn")));
            weatherNow.setSpd(cursor.getString(cursor.getColumnIndex("weather_spd")));
        }
        return weatherNow;
    }

    public List<WeatherDailyForecast> loadDailyForecast(String cityCode){
        List<WeatherDailyForecast> list = new ArrayList<>();
        Cursor cdaily = db.query("dailyforecast",null,"city_code = ?",new String[]{cityCode},null,null,"weather_date asc");
        if (cdaily.moveToFirst()){
            do {
                WeatherDailyForecast forecast = new WeatherDailyForecast();
                forecast.setAstroSr(cdaily.getString(cdaily.getColumnIndex("astro_sr")));
                forecast.setAstroSs(cdaily.getString(cdaily.getColumnIndex("astro_ss")));
                forecast.setCondCodeD(cdaily.getString(cdaily.getColumnIndex("cond_code_d")));
                forecast.setCondCodeN(cdaily.getString(cdaily.getColumnIndex("cond_code_n")));
                forecast.setCondTxtD(cdaily.getString(cdaily.getColumnIndex("cond_txt_d")));
                forecast.setCondTxtN(cdaily.getString(cdaily.getColumnIndex("cond_txt_n")));
                forecast.setForecastDate(cdaily.getString(cdaily.getColumnIndex("weather_date")));
                forecast.setForecastHum(cdaily.getString(cdaily.getColumnIndex("weather_hum")));
                forecast.setForecastPcpn(cdaily.getString(cdaily.getColumnIndex("weather_pcpn")));
                forecast.setForecastPop(cdaily.getString(cdaily.getColumnIndex("weather_pop")));
                forecast.setForecastPres(cdaily.getString(cdaily.getColumnIndex("weather_pres")));
                forecast.setForecastVis(cdaily.getString(cdaily.getColumnIndex("weather_vis")));
                forecast.setTmp_max(cdaily.getString(cdaily.getColumnIndex("tmp_max")));
                forecast.setTmp_min(cdaily.getString(cdaily.getColumnIndex("tmp_min")));
                forecast.setWindDeg(cdaily.getString(cdaily.getColumnIndex("wind_deg")));
                forecast.setWindDir(cdaily.getString(cdaily.getColumnIndex("wind_dir")));
                forecast.setWindSc(cdaily.getString(cdaily.getColumnIndex("wind_sc")));
                forecast.setWindSpd(cdaily.getString(cdaily.getColumnIndex("wind_spd")));
                list.add(forecast);
            }while (cdaily.moveToNext());
        }
        return list;
    }
}
