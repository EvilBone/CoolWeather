package com.coolweather.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.coolweather.R;
import com.coolweather.model.WeatherInfo;

import java.util.List;

/**
 * Created by bone on 16/3/31.
 */
public class CityAdapter extends ArrayAdapter<WeatherInfo>{

    private int resourceId;

    public CityAdapter(Context context, int resource, List<WeatherInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherInfo weatherInfo = getItem(position);
        View view;
        ViewHolder viewHolder;

        if(convertView != null){
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }else {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.icon_image);
            viewHolder.cityView = (TextView)view.findViewById(R.id.city_name_item_tv);
            viewHolder.tmpView = (TextView)view.findViewById(R.id.icon_tmp);
            view.setTag(viewHolder);
        }
        String tmpMax = weatherInfo.getWeatherDailyForecast().get(0).getTmp_max();
        String tmpMin = weatherInfo.getWeatherDailyForecast().get(0).getTmp_min();
        viewHolder.tmpView.setText(tmpMin+"℃/"+tmpMax+"℃");
        viewHolder.cityView.setText(weatherInfo.getCity());
        return super.getView(position, convertView, parent);
    }

    class ViewHolder{
        ImageView imageView;
        TextView tmpView;
        TextView cityView;
    }
}
