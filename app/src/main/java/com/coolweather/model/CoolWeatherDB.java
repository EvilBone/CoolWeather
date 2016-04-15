package com.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ListView;

import com.coolweather.db.CoolWeatherOpenHelper;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private Context context;

    //私有化构造函数？
    private CoolWeatherDB(Context context) {
        CoolWeatherOpenHelper dbHepler = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHepler.getWritableDatabase();
        this.context = context;
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

    //获取热门城市
    public List<City> loadHotCities() {
        List<City> cities = new ArrayList<City>();
        Cursor cursor = db.query("City", null, null, null, null, null, "city_code asc limit 24");
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                String cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                city.setCityCode(cityCode);
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                Cursor c = db.query("weatherinfo", null, "city_code = ?", new String[]{cityCode}, null, null, null);
                if (c.moveToFirst()) {
                    city.setSelected(true);
                } else {
                    city.setSelected(false);
                }
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

    //
    public WeatherInfo loadWeatherInfo(String cityCode){
        WeatherInfo weatherInfo = null;
        Cursor cursor = db.query("weatherinfo",null,"city_code = ?",new String[]{cityCode},null,null,null);
        if(cursor.moveToFirst()){
            weatherInfo = new WeatherInfo();
            weatherInfo.setId(cursor.getString(cursor.getColumnIndex("city_code")));
            weatherInfo.setCity(cursor.getString(cursor.getColumnIndex("city_name")));
            weatherInfo.setUpdateLoc(cursor.getString(cursor.getColumnIndex("update_loc")));
            weatherInfo.setUpdateUtc(cursor.getString(cursor.getColumnIndex("update_utc")));
            updateSelectedCity();
            int sel = cursor.getInt(cursor.getColumnIndex("city_select"));
            boolean isSelect = false;
            if (sel > 0)
                isSelect = true;
            weatherInfo.setCitySelect(isSelect);
            WeatherNow weatherNow = loadWeatherNow(cityCode);
            weatherInfo.setWeatherNow(weatherNow);
            List<WeatherDailyForecast> list = loadDailyForecast(cityCode);
            weatherInfo.setWeatherDailyForecast(list);
        }
        return weatherInfo;
    }

    //返回默认城市
    public String loadDefaultCity(){
        String cityCode = "";
        Cursor cursor = db.query("weatherinfo",null,"city_select = 1",null,null,null,null);
        if(cursor.moveToFirst()){
            cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
        }
        return cityCode;
    }

    private WeatherNow loadWeatherNow(String cityCode){
        WeatherNow weatherNow = null;
        Cursor cursor = db.query("weathernow",null,"city_code = ?",new String[]{cityCode},null,null,null);
        if(cursor.moveToFirst()){
            weatherNow = new WeatherNow();
            weatherNow.setCode(cursor.getString(cursor.getColumnIndex("cond_code")));
            weatherNow.setTxt(cursor.getString(cursor.getColumnIndex("cond_txt")));
            weatherNow.setSpd(cursor.getString(cursor.getColumnIndex("weather_spd")));
            weatherNow.setPcpn(cursor.getString(cursor.getColumnIndex("weather_pcpn")));
            weatherNow.setDir(cursor.getString(cursor.getColumnIndex("weather_dir")));
            weatherNow.setDeg(cursor.getString(cursor.getColumnIndex("weather_deg")));
            weatherNow.setFl(cursor.getString(cursor.getColumnIndex("weather_fl")));
            weatherNow.setHum(cursor.getString(cursor.getColumnIndex("weather_hum")));
            weatherNow.setPres(cursor.getString(cursor.getColumnIndex("weather_pres")));
            weatherNow.setSc(cursor.getString(cursor.getColumnIndex("weather_sc")));
            weatherNow.setTmp(cursor.getString(cursor.getColumnIndex("weather_tmp")));
            weatherNow.setVis(cursor.getString(cursor.getColumnIndex("weather_vis")));
            weatherNow.setIcon(getWeatherIcon(cursor.getString(cursor.getColumnIndex("cond_code"))));
        }
        return weatherNow;
    }
    //更新天气信息
    public void updateWeatherInfo(WeatherInfo weatherInfo){
        if(weatherInfo != null){
            ContentValues infovalues = new ContentValues();
            infovalues.put("city_select",1);
            db.update("weatherinfo",infovalues,"city_code = ?",new String[]{weatherInfo.getId()});
            LogUtil.d("CoolWeatherDB","updateWeatherInfo 执行成功！");
        }
    }
    //更新选择信息
    public void updateSelectedCity(){
        ContentValues values = new ContentValues();
        values.put("city_select",0);
        db.update("weatherinfo",values,null,null);
        LogUtil.d("CoolWeatherDB","updateSelectedCity 执行成功！");
    }
    //存储天气信息
    public void saveWeatherInfo(WeatherInfo weatherInfo){
        if(weatherInfo != null){
            ContentValues infovalues = new ContentValues();
            infovalues.put("city_code",weatherInfo.getId());
            infovalues.put("city_name",weatherInfo.getCity());
            infovalues.put("update_loc",weatherInfo.getUpdateLoc());
            infovalues.put("update_utc", weatherInfo.getUpdateUtc());
            infovalues.put("city_select",weatherInfo.isCitySelect());
            Cursor cursor = db.query("weatherinfo",null,"city_code = ?",new String[]{weatherInfo.getId()},null,null,null);
            if(cursor.moveToFirst()){
                db.update("weatherinfo",infovalues,"city_code = ?",new String[]{weatherInfo.getId()});
            }else {
                db.insert("weatherinfo", null, infovalues);
            }
            ContentValues nowValues = new ContentValues();
            nowValues.put("city_code",weatherInfo.getId());
            nowValues.put("cond_code",weatherInfo.getWeatherNow().getCode());
            nowValues.put("cond_txt",weatherInfo.getWeatherNow().getTxt());
            nowValues.put("weather_fl",weatherInfo.getWeatherNow().getFl());
            nowValues.put("weather_spd",weatherInfo.getWeatherNow().getSpd());
            nowValues.put("weather_sc",weatherInfo.getWeatherNow().getSc());
            nowValues.put("weather_deg",weatherInfo.getWeatherNow().getDeg());
            nowValues.put("weather_dir",weatherInfo.getWeatherNow().getDir());
            nowValues.put("weather_pcpn",weatherInfo.getWeatherNow().getPcpn());
            nowValues.put("weather_tmp",weatherInfo.getWeatherNow().getTmp());
            nowValues.put("weather_hum",weatherInfo.getWeatherNow().getHum());
            nowValues.put("weather_pres",weatherInfo.getWeatherNow().getPres());
            nowValues.put("weather_vis", weatherInfo.getWeatherNow().getVis());
            Cursor c = db.query("weathernow",null,"city_code = ?",new String[]{weatherInfo.getId()},null,null,null);
            if (c.moveToFirst()){
                db.update("weathernow",nowValues,"city_code = ?",new String[]{weatherInfo.getId()});
            }else {
                db.insert("weathernow", null, nowValues);
            }
            ContentValues dailyValues = new ContentValues();
            dailyValues.put("city_code",weatherInfo.getId());
            saveDailyForecast(weatherInfo.getId(),weatherInfo.getWeatherDailyForecast());

        }
    }

    private void saveDailyForecast(String cityCode,List<WeatherDailyForecast> list){
        for(WeatherDailyForecast forecast:list){
            db.delete("dailyforecast","city_code = ?",new String[]{cityCode});
            ContentValues values = new ContentValues();
            values.put("city_code",cityCode);
            values.put("weather_date",forecast.getForecastDate());
            values.put("astro_sr",forecast.getAstroSr());
            values.put("astro_ss",forecast.getAstroSs());
            values.put("cond_code_d",forecast.getCondCodeD());
            values.put("cond_code_n",forecast.getCondCodeN());
            values.put("cond_txt_d",forecast.getCondTxtD());
            values.put("cond_txt_n",forecast.getCondTxtN());
            values.put("weather_hum",forecast.getForecastHum());
            values.put("weather_pcpn",forecast.getForecastPcpn());
            values.put("weather_pop",forecast.getForecastPop());
            values.put("weather_pres",forecast.getForecastPres());
            values.put("weather_vis",forecast.getForecastVis());
            values.put("tmp_max",forecast.getTmp_max());
            values.put("tmp_min",forecast.getTmp_min());
            values.put("wind_seg",forecast.getWindDeg());
            values.put("wind_dir",forecast.getWindDir());
            values.put("wind_sc",forecast.getWindSc());
            values.put("wind_spd",forecast.getWindSpd());
            db.insert("dailyforecast",null,values);
        }
    }

    //获取天气信息
    public List<WeatherInfo> loadWeatherInfos() {
        List<WeatherInfo> infoList = new ArrayList<>();
        Cursor cursor = db.query("weatherinfo", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                WeatherInfo weatherInfo = new WeatherInfo();
                String cityCode = cursor.getString(cursor.getColumnIndex("city_code"));
                weatherInfo.setId(cityCode);
                weatherInfo.setCity(cursor.getString(cursor.getColumnIndex("city_name")));
                int sel = cursor.getInt(cursor.getColumnIndex("city_select"));
                boolean isSelect = false;
                if (sel > 0)
                    isSelect = true;
                weatherInfo.setCitySelect(isSelect);
                weatherInfo.setUpdateLoc(cursor.getString(cursor.getColumnIndex("update_loc")));
                weatherInfo.setUpdateUtc(cursor.getString(cursor.getColumnIndex("update_utc")));
                WeatherNow weatherNow = loadWeatherNow(cityCode);
                weatherInfo.setWeatherNow(weatherNow);
                List<WeatherDailyForecast> forecast = loadDailyForecast(cityCode);
                weatherInfo.setWeatherDailyForecast(forecast);
                infoList.add(weatherInfo);
            } while (cursor.moveToNext());
        }
        return infoList;
    }

    private Bitmap getWeatherIcon(String condCode){
        Bitmap bitmap = null;
            try {
                FileInputStream fileInputStream = context.openFileInput(condCode);
                bitmap = BitmapFactory.decodeStream(fileInputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

        return bitmap;
    }

    public List<WeatherDailyForecast> loadDailyForecast(String cityCode) {
        List<WeatherDailyForecast> list = new ArrayList<>();
        Cursor cdaily = db.query("dailyforecast", null, "city_code = ?", new String[]{cityCode}, null, null, "weather_date asc");
        if (cdaily.moveToFirst()) {
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
//                forecast.setWindDeg(cdaily.getString(cdaily.getColumnIndex("wind_deg")));
                forecast.setWindDir(cdaily.getString(cdaily.getColumnIndex("wind_dir")));
                forecast.setWindSc(cdaily.getString(cdaily.getColumnIndex("wind_sc")));
                forecast.setWindSpd(cdaily.getString(cdaily.getColumnIndex("wind_spd")));
                list.add(forecast);
            } while (cdaily.moveToNext());
        }
        return list;
    }
}
