package com.coolweather.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.R;
import com.coolweather.model.City;
import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.Country;
import com.coolweather.model.Province;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends AppCompatActivity {
    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_COUNTRY = 0;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CoolWeatherDB coolWeatherDB;
    private List<String> dataList = new ArrayList<>();

    //城市列表
    private List<City> cities;

    //省份
    private List<Province> provinces;

    //国家
    private List<Country> countries;

    private Country selectedCountry;

    private Province selectedProvince;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        titleText = (TextView) findViewById(R.id.title_text);
        listView = (ListView) findViewById(R.id.list_View);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        coolWeatherDB = CoolWeatherDB.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_COUNTRY) {
                    selectedCountry = countries.get(position);
                    queryProvinces();
                } else if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinces.get(position);
                    queryCities();
                }
            }
        });
        queryCountries();
    }

    private void queryCountries() {
        countries = coolWeatherDB.loadCountries();
        if (countries.size() > 0) {
            dataList.clear();
            for (Country country : countries) {
                dataList.add(country.getCountryName());
            }
            Log.d(getClass().getName(), "dataList size is " + String.valueOf(dataList.size()));
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("国家");
            currentLevel = LEVEL_COUNTRY;
        } else {
            queryFromSever();
        }

    }

    private void queryProvinces() {
        provinces = coolWeatherDB.loadProvinces(selectedCountry.getId());
        if (provinces.size() > 0) {
            dataList.clear();
            for (Province province : provinces) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCountry.getCountryName());
            currentLevel = LEVEL_PROVINCE;
        } else {
        }
    }

    private void queryCities() {
        cities = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cities.size() > 0) {
            dataList.clear();
            for (City city : cities) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {

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
                            queryCountries();
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
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else if (currentLevel == LEVEL_PROVINCE) {
            queryCountries();
        } else {
            finish();
        }
    }
}
