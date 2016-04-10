package com.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.coolweather.R;
import com.coolweather.model.BaseActivity;
import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.Country;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.Utility;

import java.util.List;

/**
 * Created by bone on 16/3/31.
 */
public class ChooseCityActivity extends BaseActivity {
    private ImageButton backBtn;
    private ProgressDialog progressDialog;
    private CoolWeatherDB coolWeatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_city);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        queryCountries();
        backBtn = (ImageButton)findViewById(R.id.back_city_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void queryCountries() {
        List<Country> countries = coolWeatherDB.loadCountries();
        if (countries.size() > 0) {
        } else {
            queryFromSever();
        }

    }
    private void queryFromSever() {
        String address = "https://api.heweather.com/x3/citylist?search=allchina&key=35351ab80a4b4e5bbac3c8f8fa7b469f";
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = Utility.handleProvincesResponse(coolWeatherDB, response);
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
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
                        Toast.makeText(ChooseCityActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
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
