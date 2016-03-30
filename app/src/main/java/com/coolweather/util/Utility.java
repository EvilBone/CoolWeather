package com.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.coolweather.model.City;
import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.Country;
import com.coolweather.model.Province;

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

}
