package com.coolweather;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.coolweather.model.CoolWeatherDB;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.Utility;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {

        super(Application.class);
    }

    public void testName() {

        CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(getContext());
        int size = coolWeatherDB.loadHotCities().size();
        Log.d("testName",String.valueOf(size));

    }

    public void testUtitlity() throws Exception {
        String address = "https://api.heweather.com/x3/citylist?search=allworld&key=35351ab80a4b4e5bbac3c8f8fa7b469f";
        Log.d("TestUtitlity", "address is " + address);
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.d("TestUtitlity", "Start ....");
                CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(getContext());
                boolean result = Utility.handleProvincesResponse(coolWeatherDB, response);
                Log.d("TestUtitlity", String.valueOf(result));
                Log.d("TestUtitlity", "End ....");
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}