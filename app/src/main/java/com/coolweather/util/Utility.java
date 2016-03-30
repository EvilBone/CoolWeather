package com.coolweather.util;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.model.City;
import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.Country;
import com.coolweather.model.Province;
import com.coolweather.model.WeatherInfo;
import com.coolweather.model.WeatherNow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bone on 16/3/29.
 */
public class Utility {

    public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                Log.d("Utility", "Start....");
                JSONArray jsonArray = new JSONObject(response).getJSONArray("city_info");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String cnty = jsonObject.getString("cnty");
                    String cityName = jsonObject.getString("city");
                    String prov = jsonObject.getString("prov");
                    String cityCode = jsonObject.getString("id");
                    String lac = jsonObject.getString("lat");
                    String lon = jsonObject.getString("lon");
                    Country cn = coolWeatherDB.loadCountry(cnty);
                    if (cn == null) {
                        Country country = new Country();
                        country.setCountryName(cnty);
                        country = coolWeatherDB.saveCountry(country);
                        cn = country;
                    }
                    Province pro = coolWeatherDB.loadProvince(prov);
                    if (pro == null) {
                        Province province = new Province();
                        province.setProvinceName(prov);
                        province.setCountryId(cn.getId());
                        province = coolWeatherDB.saveProvince(province);
                        pro = province;
                    }
                    City city = new City();
                    city.setProvinceId(pro.getId());
                    city.setCityName(cityName);
                    city.setCityCode(cityCode);
                    city.setCityLac(lac);
                    city.setCityLon(lon);
                    coolWeatherDB.saveCity(city);
                }
                Log.d("Utility", "End....");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public synchronized static boolean handleWeathrResponse(SharedPreferences preferences, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response).getJSONArray("HeWeather data service 3.0").getJSONObject(0);
                JSONObject basicJson = jsonObject.getJSONObject("basic");
                String cityName = basicJson.getString("city");
                String countryName = basicJson.getString("cnty");
                String cityCode = basicJson.getString("id");
                JSONObject updateJson = basicJson.getJSONObject("update");
                String update = updateJson.getString("loc");
                String utc = updateJson.getString("utc");
                //基本信息
                WeatherInfo weatherInfo = new WeatherInfo();
                weatherInfo.setCity(cityName);
                weatherInfo.setCnty(countryName);
                weatherInfo.setId(cityCode);
                weatherInfo.setUpdateLoc(update);
                weatherInfo.setUpdateUtc(utc);
                JSONObject nowJson = jsonObject.getJSONObject("now");
                JSONObject condJson = nowJson.getJSONObject("cond");
                JSONObject windJson = nowJson.getJSONObject("wind");
                WeatherNow weatherNow = new WeatherNow();
                //天气状况
                weatherNow.setCode(condJson.getString("code"));
                weatherNow.setTxt(condJson.getString("txt"));
                //风力
                weatherNow.setDeg(windJson.getString("deg"));
                weatherNow.setDir(windJson.getString("dir"));
                weatherNow.setSpd(windJson.getString("spd"));
                weatherNow.setSc(windJson.getString("sc"));

                weatherNow.setFl(nowJson.getString("fl"));
                weatherNow.setHum(nowJson.getString("hum"));
                weatherNow.setPcpn(nowJson.getString("pcpn"));
                weatherNow.setPres(nowJson.getString("pres"));
                weatherNow.setTmp(nowJson.getString("tmp"));
                weatherNow.setVis(nowJson.getString("vis"));

                weatherInfo.setWeatherNow(weatherNow);
//存储天气数据
                SharedPreferences.Editor editor = preferences.edit();

                editor.putString("city", weatherInfo.getCity());
                editor.putString("cnty", weatherInfo.getCnty());
                editor.putString("cityCode", weatherInfo.getId());
                editor.putString("updateLoc", weatherInfo.getUpdateLoc());
                editor.putString("updateUtc", weatherInfo.getUpdateLoc());
                editor.putString("condCode", weatherNow.getCode());
                editor.putString("condTxt", weatherNow.getTxt());
                editor.putString("tmp", weatherNow.getTmp());
                editor.commit();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }


}
