package com.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.coolweather.R;
import com.coolweather.model.WeatherInfo;

import java.util.List;

public class CitiesActivity extends Activity implements View.OnClickListener {
    private ImageButton backBtn;
    private ImageButton addBtn;
    private ListView listView;
    private List<WeatherInfo> weatherInfoList;
    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cities);
        backBtn = (ImageButton) findViewById(R.id.back_btn);
        addBtn = (ImageButton) findViewById(R.id.add_btn);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_View);
        adapter = new CityAdapter(this,R.layout.city_item,weatherInfoList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_btn:
                Toast.makeText(this,"this is a add action",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}
