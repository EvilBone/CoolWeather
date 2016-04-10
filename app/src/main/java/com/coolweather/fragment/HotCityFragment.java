package com.coolweather.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.coolweather.R;
import com.coolweather.activity.ChooseAreaActivity;
import com.coolweather.activity.CityGVAdapter;
import com.coolweather.activity.WeatherActivity;
import com.coolweather.model.City;
import com.coolweather.model.CoolWeatherDB;

import java.util.List;

/**
 * Created by bone on 16/3/31.
 */
public class HotCityFragment extends Fragment implements AdapterView.OnItemClickListener {
    private GridView gridView;
    private CityGVAdapter adapter;
    private List<City> cities;
    private CoolWeatherDB coolWeatherDB;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotcity_frag,container,false);
        gridView = (GridView)view.findViewById(R.id.hotcity_gv);
        coolWeatherDB = CoolWeatherDB.getInstance(getActivity());
        cities = coolWeatherDB.loadHotCities();
        adapter = new CityGVAdapter(getActivity(),R.layout.city_item_gv,cities);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        City city = cities.get(position);
        Intent intent = new Intent(getActivity(),WeatherActivity.class);
        intent.putExtra("cityCode",city.getCityCode());
        startActivity(intent);
    }


}
