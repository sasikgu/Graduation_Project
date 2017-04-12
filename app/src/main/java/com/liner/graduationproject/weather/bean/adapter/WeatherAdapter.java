package com.liner.graduationproject.weather.bean.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.liner.graduationproject.R;
import com.liner.graduationproject.weather.bean.WeatherShowApi;
import com.liner.graduationproject.weather.bean.WeatherdayList;
import com.rock.teachlibrary.ImageLoader;
import com.rock.teachlibrary.adapters.TeachMultiTypeBaseAdapter;

import java.util.List;

/**
 *
 *
 */

public class WeatherAdapter extends TeachMultiTypeBaseAdapter<WeatherdayList> {


    public WeatherAdapter(Context context, List<WeatherdayList> data, int... layoutIds) {
        super(context, data, layoutIds);
    }

    @Override
    public void bindData(ViewHolder holder, WeatherdayList item, int position) {
        holder.setText(R.id.weather_daytime,item.getDaytime());
        holder.setText(R.id.weather_night_weather,item.getNight_weather());
        holder.setText(R.id.day_air_temperature,item.getDay_air_temperature()+"/"+item.getNight_air_temperature()+"℃");
//        holder.setText(R.id.night_air_temperature,item.getNight_air_temperature());
        ImageView holderView = (ImageView) holder.findView(R.id.weacher_day_weather_pic);
        ImageLoader.display(holderView,item.getDay_weather_pic());

    }
}
