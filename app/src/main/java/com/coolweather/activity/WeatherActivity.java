package com.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.R;
import com.coolweather.model.BaseActivity;
import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.WeatherInfo;
import com.coolweather.service.AutoUpdateService;
import com.coolweather.util.ActivityController;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpCallbackListenerFile;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.LogUtil;
import com.coolweather.util.Utility;

import java.io.InputStream;

public class WeatherActivity extends BaseActivity implements View.OnClickListener {
    private TextView dateTV;//当前日期
    private TextView updateTV;//更新时间
    private TextView cityTV;//城市
    private TextView tmpTV;//温度
    private TextView condTV;//天气情况
    private ImageButton refreshImageBtn;//刷新
    private ImageButton switchImageBtn;//切换城市
    private LinearLayout weatherLayout;
    private ProgressDialog progressDialog;
    private static final String KEY = "35351ab80a4b4e5bbac3c8f8fa7b469f";
    private static final String HOSTADRESS = "https://api.heweather.com/x3/weather?cityid=";
    private WeatherInfo weatherInfo;
    private CoolWeatherDB coolWeatherDB;
    private String cityCode;
    //上次按下返回键的系统时间
    private long lastBackTime = 0;
    //当前按下返回键的系统时间
    private long currentBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);
        coolWeatherDB = CoolWeatherDB.getInstance(this);

        dateTV = (TextView) findViewById(R.id.date_textview);
        updateTV = (TextView) findViewById(R.id.update_tv);
        cityTV = (TextView) findViewById(R.id.city_name_textview);
        tmpTV = (TextView) findViewById(R.id.tmp_textview);
        condTV = (TextView) findViewById(R.id.cond_textview);
        refreshImageBtn = (ImageButton) findViewById(R.id.refresh_btn);
        switchImageBtn = (ImageButton) findViewById(R.id.switch_btn);
        weatherLayout = (LinearLayout) findViewById(R.id.weather_layout);
        weatherLayout.setVisibility(View.INVISIBLE);

        refreshImageBtn.setOnClickListener(this);
        switchImageBtn.setOnClickListener(this);

        Intent intent = getIntent();
        cityCode = intent.getStringExtra("cityCode");
        LogUtil.d("WeatherActivity", "cityCode is " + cityCode);
        if (!TextUtils.isEmpty(cityCode)) {
            queryWeatherInfo();
        } else {
            cityCode = coolWeatherDB.loadDefaultCity();
            if ("".equals(cityCode)) {
                Intent tent = new Intent(WeatherActivity.this, ChooseCityActivity.class);
                startActivity(tent);
            } else {
                queryWeatherInfo();
            }
        }
        Intent i = new Intent(this, AutoUpdateService.class);
        startService(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获返回键按下的事件
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if (currentBackTime - lastBackTime > 2 * 1000) {
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            } else { //如果两次按下的时间差小于2秒，则退出程序
                ActivityController.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refresh_btn:
                queryWeatherInfoFromServer();
                break;
            case R.id.switch_btn:
                Intent intent = new Intent(WeatherActivity.this, CitiesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }


    //查询天气信息
    private void queryWeatherInfo() {
        weatherInfo = coolWeatherDB.loadWeatherInfo(cityCode);
        if (weatherInfo != null) {
            showWeatherInf();
            coolWeatherDB.updateSelectedCity();
            weatherInfo.setCitySelect(true);
            coolWeatherDB.updateWeatherInfo(weatherInfo);
        } else {
            queryWeatherInfoFromServer();
        }

    }

    private void queryWeatherInfoFromServer() {
        String address = HOSTADRESS + cityCode + "&key=" + KEY;

        showProgressDialog();

        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Boolean result = Utility.handleWeathrResponse(coolWeatherDB, response);
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            weatherInfo = coolWeatherDB.loadWeatherInfo(cityCode);
                            saveIcon();
                            showWeatherInf();
                            coolWeatherDB.updateSelectedCity();
                            weatherInfo.setCitySelect(true);
                            coolWeatherDB.updateWeatherInfo(weatherInfo);
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(WeatherActivity.this, "更新失败", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void saveIcon() {
        String addressIcon = "http://www.heweather.com/weather/images/icon/" + weatherInfo.getWeatherNow().getCode() + ".png";
        HttpUtil.sendHttpRequestForFile(this, weatherInfo.getWeatherNow().getCode(), addressIcon, new HttpCallbackListenerFile() {
            @Override
            public void onFinish(Bitmap bitmap) {
                Log.d("WeatherActivity", "CityCode is " + cityCode);
                weatherInfo.getWeatherNow().setIcon(bitmap);
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void showWeatherInf() {
        cityTV.setText(weatherInfo.getCity());
        String date = weatherInfo.getUpdateLoc();
        dateTV.setText(date.substring(0, 10));
        updateTV.setText(date.substring(10, 16) + "发布");
        tmpTV.setText(weatherInfo.getWeatherNow().getTmp() + "℃");
        condTV.setText(weatherInfo.getWeatherNow().getTxt());
        weatherLayout.setVisibility(View.VISIBLE);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在更新...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
