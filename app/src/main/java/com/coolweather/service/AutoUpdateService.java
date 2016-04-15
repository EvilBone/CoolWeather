package com.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.Toast;

import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.WeatherInfo;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpCallbackListenerFile;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.LogUtil;
import com.coolweather.util.Utility;

import java.util.List;

/**
 * Created by bone on 16/4/10.
 */
public class AutoUpdateService extends Service{
    private static final String KEY = "35351ab80a4b4e5bbac3c8f8fa7b469f";
    private static final String HOSTADRESS = "https://api.heweather.com/x3/weather?cityid=";
    CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(this);
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("AutoUpdateService","updateWeather Start.."+SystemClock.elapsedRealtime());
                updateWeather();
                LogUtil.d("AutoUpdateService","updateWeather End.."+SystemClock.elapsedRealtime());
            }
        }).start();

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 1*60*60*60;
        long triggerAtTime = SystemClock.elapsedRealtime()+anHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        List<WeatherInfo> list =coolWeatherDB.loadWeatherInfos();
        for(WeatherInfo weatherInfo:list){
            queryWeatherInfoFromServer(weatherInfo);
        }
    }
        private void queryWeatherInfoFromServer(final WeatherInfo weatherInfo) {
            String address = HOSTADRESS + weatherInfo.getId() + "&key=" + KEY;
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    Boolean result = Utility.handleWeathrResponse(coolWeatherDB, response);
                    if (result) {
                                saveIcon(weatherInfo);
                            }
                        }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    private void saveIcon(final WeatherInfo weatherInfo) {
        String addressIcon = "http://www.heweather.com/weather/images/icon/" + weatherInfo.getWeatherNow().getCode() + ".png";
        HttpUtil.sendHttpRequestForFile(this, weatherInfo.getWeatherNow().getCode(), addressIcon, new HttpCallbackListenerFile() {
            @Override
            public void onFinish(Bitmap bitmap) {
                Log.d("WeatherActivity", "CityCode is " + weatherInfo.getId());
                weatherInfo.getWeatherNow().setIcon(bitmap);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
