package com.coolweather.model;

import java.util.List;

/**
 * Created by bone on 16/3/30.
 */
public class WeatherInfo {
    private String city;
    private String cnty;
    private String id;
    private String updateLoc;//数据更新的当地时间
    private String updateUtc;//数据更新的国际时间
    private boolean citySelect;
    private WeatherNow weatherNow;
    private List<WeatherDailyForecast> weatherDailyForecast;

    public List<WeatherDailyForecast> getWeatherDailyForecast() {
        return weatherDailyForecast;
    }

    public void setWeatherDailyForecast(List<WeatherDailyForecast> weatherDailyForecast) {
        this.weatherDailyForecast = weatherDailyForecast;
    }

    public boolean isCitySelect() {
        return citySelect;
    }

    public void setCitySelect(boolean citySelect) {
        this.citySelect = citySelect;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateLoc() {
        return updateLoc;
    }

    public void setUpdateLoc(String updateLoc) {
        this.updateLoc = updateLoc;
    }

    public String getUpdateUtc() {
        return updateUtc;
    }

    public void setUpdateUtc(String updateUtc) {
        this.updateUtc = updateUtc;
    }

    public WeatherNow getWeatherNow() {
        return weatherNow;
    }

    public void setWeatherNow(WeatherNow weatherNow) {
        this.weatherNow = weatherNow;
    }
}
