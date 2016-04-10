package com.coolweather.activity;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.coolweather.R;
import com.coolweather.model.City;

import java.util.List;

/**
 * Created by bone on 16/3/31.
 */
public class CityGVAdapter extends ArrayAdapter<City>{
    private int resourceId;
    public CityGVAdapter(Context context, int resource, List<City> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        View view;

        if(convertView != null){
            view = convertView;
        }else {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        }
        TextView cityTV = (TextView)view.findViewById(R.id.city_name_item_gv);
        cityTV.setText(city.getCityName());
        if(city.isSelected()){
            cityTV.setSelected(true);
        }else {
            cityTV.setSelected(false);
        }
        return view;
    }
}
