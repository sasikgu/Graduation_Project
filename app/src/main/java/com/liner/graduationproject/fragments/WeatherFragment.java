package com.liner.graduationproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liner.graduationproject.R;
import com.liner.graduationproject.weather.bean.WeatherDataList;
import com.liner.graduationproject.weather.bean.WeatherShowApi;
import com.liner.graduationproject.weather.bean.WeatherdayList;
import com.liner.graduationproject.weather.bean.adapter.WeatherAdapter;
import com.rock.teachlibrary.http.HttpUtil;
import com.rock.teachlibrary.http.callback.RequestCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/4/10/010.
 */

public class WeatherFragment extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener {

    public static final String WETHER_URL = "http://route.showapi.com/9-9?showapi_appid=19589&showapi_sign=84a7b99dab6a45b99f0dbc36c69c7a61&area=";
    private static final String TAG = WeatherFragment.class.getSimpleName();
    private static final int SEND_TIME_DELE = 100;
    private EditText mCityName;

    private View layout;
    private ListView mListView;
    private WeatherAdapter adapter;
    //设置头布局中的天气信息
    TextView weather_header_area;
    private TextView weather_header_daytime;
    private ImageView weather_header_day_weather_pic;
    private TextView weather_header_day_weather;
    private TextView weather_header_day_air_temperature;
    private TextView weather_night_wind_power;
    private TextView weather_big_size;
    private TextView weather_big_size2;

    List<WeatherdayList> dayList;
    private ImageView weather_big_pic;
    AlphaAnimation alphaAnimation;

    int image[]=new int[]{R.mipmap.weather_chun,R.mipmap.weather_xia,R.mipmap.weather_qiu,R.mipmap.weather_dong};
    private int currentNum=0;
    //1 设置天气预报背景图片的轮播
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case SEND_TIME_DELE:
                    //TODO 进行轮换背景图片的操作
//                    for (int i = 0; i <image.length; i++) {
                    layout.setBackgroundResource(image[currentNum%4]);
                    currentNum++;
                    //回调自己
                    mHandler.sendEmptyMessageDelayed(SEND_TIME_DELE,5000);
//                    }
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        Log.e(TAG, "onCreateView: " );
        layout = inflater.inflate(R.layout.fragment_weather, container, false);

        initView();
        return layout;
    }


    private void initView() {

        mListView = ((ListView) layout.findViewById(R.id.weather_lv));
        /**
         *    给ListView添加一个头布局，采用布局填充器的方式
         *    addHeaderView()必须在setAdapter()之前调用
         *    采用inflate(R.layout.weather_header,null) 两个参数的布局，LayoutParam参数会丢失
         *    采用inflate(R.layout.weather_header,mListView,false)三个参数的布局，LayoutParam的参数才不会丢失
         * */

//      RelativeLayout headView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.weather_header,null);
        RelativeLayout headView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.weather_header, mListView, false);
        mListView.addHeaderView(headView);


        mCityName = (EditText) layout.findViewById(R.id.weather_city);
        layout.findViewById(R.id.weather_seacher_bt).setOnClickListener(this);
        adapter = new WeatherAdapter(getContext(), null, R.layout.weather_item);
        mListView.setAdapter(adapter);

        /**
         * 默认加载北京的天气
         *      1 找到控件
         *      2 设置到布局上面
         * */
        weather_header_area = (TextView) layout.findViewById(R.id.weather_header_area);
        weather_header_daytime = (TextView) layout.findViewById(R.id.weather_header_daytime);
        //TODO 加载图片
        weather_header_day_weather_pic = ((ImageView) layout.findViewById(R.id.weather_header_day_weather_pic));
        weather_big_pic = ((ImageView) layout.findViewById(R.id.weather_big_pic));
        /**
         * 给天气的图标设置一个渐变动画
         * */
        alphaAnimation = new AlphaAnimation(0.4f, 1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setRepeatCount(-1);
        //渐变的反复
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        weather_big_pic.startAnimation(alphaAnimation);
        Log.e(TAG, "initView: 执行动画的渐变" );
        //设置ListView的滑动监听事件
        mListView.setOnScrollListener(this);

        weather_header_day_weather = (TextView) layout.findViewById(R.id.weather_header_day_weather);
        weather_header_day_air_temperature = ((TextView) layout.findViewById(R.id.weather_header_day_air_temperature));
        weather_night_wind_power = ((TextView) layout.findViewById(R.id.weather_night_wind_power));
        //大字体
        weather_big_size = ((TextView) layout.findViewById(R.id.weather_big_size));
        weather_big_size2 = (TextView) layout.findViewById(R.id.weather_big_size2);
        //------------默认加载北京的天气--------------------------------------
        HttpUtil.getStringAsync(WETHER_URL + "北京", new RequestCallback() {
            @Override
            public void onSucceed(String result) {
                Gson gson = new Gson();
                WeatherDataList dataList = gson.fromJson(result, WeatherDataList.class);

                WeatherShowApi showapi_res_body = dataList.getShowapi_res_body();
                List<WeatherdayList> dayList = showapi_res_body.getDayList();
                weather_header_area.setText(showapi_res_body.getArea());

                //默认加载头布局的天气
                weather_night_wind_power.setText(dayList.get(0).getDay_wind_direction() + " " + dayList.get(0).getDay_wind_power());
                weather_header_daytime.setText("日期:"+dayList.get(0).getDaytime());
                Picasso.with(getContext()).load(dayList.get(0).getDay_weather_pic()).into(weather_header_day_weather_pic);
                weather_header_day_weather.setText(dayList.get(0).getDay_weather());
                weather_header_day_air_temperature.setText(dayList.get(0).getDay_air_temperature() + "-" + dayList.get(0).getNight_air_temperature() + "℃");
                //大字体
                weather_big_size.setText(dayList.get(0).getDay_air_temperature());
                weather_big_size2.setText(dayList.get(0).getNight_air_temperature());
                //大图片
                Picasso.with(getContext()).load(dayList.get(0).getDay_weather_pic()).into(weather_big_pic);

//                Log.e(TAG, "onSucceed: "+showapi_res_body.getDayList().size() );
                adapter.updateRes(showapi_res_body.getDayList());

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onPrepare() {

            }

            @Override
            public void onLoadFinish() {

            }
        });

        //设置天气背景图片的轮播
        mHandler.sendEmptyMessageDelayed(SEND_TIME_DELE,5000);


    }

    //--------------------------------Button的点击事件--------------------------------------

    @Override
    public void onClick(View v) {

        String mCity = mCityName.getText().toString().trim();
        Log.e(TAG, "onClick: ----->" + mCity);

        if (mCity.equals("")) {
            Log.e(TAG, "initView: 点击事件为空");
            Toast.makeText(getContext(), "城市名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e(TAG, "initView: " + mCity);

        Log.e(TAG, "onClick: " + WETHER_URL + mCity);
        HttpUtil.getStringAsync(WETHER_URL + mCity, new RequestCallback() {

            @Override
            public void onSucceed(String result) {

                Gson gson = new Gson();
                WeatherDataList dataList = gson.fromJson(result, WeatherDataList.class);
                WeatherShowApi showapi_res_body = dataList.getShowapi_res_body();
                dayList = showapi_res_body.getDayList();
                weather_header_area.setText(showapi_res_body.getArea());


                //默认加载头布局的天气
                weather_night_wind_power.setText(dayList.get(0).getDay_wind_direction() + " " + dayList.get(0).getDay_wind_power());
                weather_header_daytime.setText(dayList.get(0).getDaytime());
                Picasso.with(getContext()).load(dayList.get(0).getDay_weather_pic()).into(weather_header_day_weather_pic);
                weather_header_day_weather.setText(dayList.get(0).getDay_weather());
                weather_header_day_air_temperature.setText(dayList.get(0).getDay_air_temperature() + "-" + dayList.get(0).getNight_air_temperature()+"℃");
                //大字体
                weather_big_size.setText(dayList.get(0).getDay_air_temperature());
                weather_big_size2.setText(dayList.get(0).getNight_air_temperature());
                //大图片
                Picasso.with(getContext()).load(dayList.get(0).getDay_weather_pic()).into(weather_big_pic);

//                Log.e(TAG, "onSucceed: "+showapi_res_body.getDayList().size() );

                adapter.updateRes(showapi_res_body.getDayList());

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onPrepare() {

            }

            @Override
            public void onLoadFinish() {

            }
        });


    }
//ListView的滑动监听事件
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (visibleItemCount>10) {
            weather_big_pic.startAnimation(alphaAnimation);
        }

    }
}
