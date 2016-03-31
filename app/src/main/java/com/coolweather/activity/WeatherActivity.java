package com.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.R;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weather);

        dateTV = (TextView) findViewById(R.id.date_textview);
        updateTV = (TextView) findViewById(R.id.update_tv);
        cityTV = (TextView) findViewById(R.id.city_name_textview);
        tmpTV = (TextView) findViewById(R.id.tmp_textview);
        condTV = (TextView) findViewById(R.id.cond_textview);
        refreshImageBtn = (ImageButton) findViewById(R.id.refresh_btn);
        switchImageBtn = (ImageButton) findViewById(R.id.switch_btn);
        weatherLayout = (LinearLayout)findViewById(R.id.weather_layout);
        weatherLayout.setVisibility(View.INVISIBLE);

        refreshImageBtn.setOnClickListener(this);
        switchImageBtn.setOnClickListener(this);

        Intent intent = getIntent();
        String cityCode = intent.getStringExtra("cityCode");
        if (!TextUtils.isEmpty(cityCode)) {
            queryWeatherInfo(cityCode);
        }else{

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.refresh_btn:
                SharedPreferences preferences = getSharedPreferences("weatherInfo",MODE_PRIVATE);
                String cityCode = preferences.getString("cityCode","");
                queryWeatherInfoFromServer(preferences,cityCode);
                break;
            case R.id.switch_btn:
                Intent intent = new Intent(WeatherActivity.this,CitiesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    //查询天气信息
    private void queryWeatherInfo(String cityCode) {
        SharedPreferences preferences = getSharedPreferences("weatherInfo", Context.MODE_PRIVATE);
        String code = preferences.getString("cityCode", "");
        if (cityCode != ""&& code.equals(cityCode)) {
            showWeatherInf();
        } else {
            queryWeatherInfoFromServer(preferences,cityCode);
        }

    }

    private void queryWeatherInfoFromServer(final SharedPreferences preferences, final String cityCode) {
        String address = HOSTADRESS +cityCode+"&key="+KEY;
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
               Boolean result =  Utility.handleWeathrResponse(preferences, response);
                if(result){

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            showWeatherInf();
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
                        Toast.makeText(WeatherActivity.this,"更新失败",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
    private void showWeatherInf(){
        SharedPreferences preferences = getSharedPreferences("weatherInfo", Context.MODE_PRIVATE);
        String cityName = preferences.getString("city", "");
        if (cityName != "") {
            cityTV.setText(cityName);
            String date = preferences.getString("updateLoc", "");
            dateTV.setText(date.substring(0,10));
            updateTV.setText(date.substring(10,16)+"发布");
            tmpTV.setText(preferences.getString("tmp","")+"℃");
            condTV.setText(preferences.getString("condTxt",""));
            weatherLayout.setVisibility(View.VISIBLE);
        }
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
