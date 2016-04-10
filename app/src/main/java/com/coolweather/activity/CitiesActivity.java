package com.coolweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.R;
import com.coolweather.model.BaseActivity;
import com.coolweather.model.CoolWeatherDB;
import com.coolweather.model.WeatherInfo;

import java.util.List;

public class CitiesActivity extends BaseActivity implements View.OnClickListener {
    private ImageButton backBtn;
    private ImageButton addBtn;
    private ListView listView;
    private List<WeatherInfo> weatherInfoList;
    private CityAdapter adapter;
    private CoolWeatherDB coolWeatherDB;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cities);
        imageView = (ImageView)findViewById(R.id.icon_image);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        addBtn = (ImageButton) findViewById(R.id.add_btn);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_View);
        coolWeatherDB = CoolWeatherDB.getInstance(this);
        weatherInfoList = coolWeatherDB.loadWeatherInfos();
        adapter = new CityAdapter(this, R.layout.city_item, weatherInfoList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WeatherInfo weatherInfo = weatherInfoList.get(position);
                Intent intent = new Intent(CitiesActivity.this,WeatherActivity.class);
                intent.putExtra("cityCode",weatherInfo.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_btn:
                Intent intent = new Intent(CitiesActivity.this, ChooseCityActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
