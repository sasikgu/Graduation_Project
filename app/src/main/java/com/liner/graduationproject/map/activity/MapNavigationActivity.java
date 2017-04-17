package com.liner.graduationproject.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.model.Poi;
import com.amap.api.navi.model.NaviLatLng;
import com.liner.graduationproject.R;
import com.liner.graduationproject.map.beans.PoiBean;
import com.liner.graduationproject.map.utils.LocationUtil;
import com.liner.graduationproject.map.utils.NavigationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导航Activity, 进行路线规划，完成后跳转回MapFragment地图页面
 */
public class MapNavigationActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int END = 100;
    public static final int START = 111;

    private EditText mStartLongitudeEdt;
    private EditText mStartLatitudeEdt;
    private EditText mEndLongitudeEdt;
    private EditText mEndLatitudeEdt;

    // 工具类
    private NavigationUtil mNaviUtil;
    // 模型类
    private InnerLocationBean locationBean;

    // CheckBox
    private CheckBox avoidhightspeed;
    private CheckBox hightspeed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navigation);
        initView();
    }

    protected void initView() {
        // 初始化工具类
        mNaviUtil = new NavigationUtil();
        // 初始化模型类
        locationBean = new InnerLocationBean();
        // poi搜索获取坐标
        ((ImageView) findViewById(R.id.navigation_start_poi)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.navigation_end_poi)).setOnClickListener(this);
        // 初始化坐标输入框
        mStartLongitudeEdt = (EditText) findViewById(R.id.navi_start_longitude);
        mStartLatitudeEdt = (EditText) findViewById(R.id.navi_start_latitude);
        mEndLongitudeEdt = (EditText) findViewById(R.id.navi_end_longitude);
        mEndLatitudeEdt = (EditText) findViewById(R.id.navi_end_latitude);

        // 设置选中事件及其监听事件
        ((CheckBox) findViewById(R.id.navi_option_congestion)).setOnCheckedChangeListener(this);
        avoidhightspeed = (CheckBox) findViewById(R.id.navi_option_avoidhightspeed);
        ((CheckBox) findViewById(R.id.navi_option_cost)).setOnCheckedChangeListener(this);
        hightspeed = (CheckBox) findViewById(R.id.navi_option_hight_speed);
        avoidhightspeed.setOnCheckedChangeListener(this);
        hightspeed.setOnCheckedChangeListener(this);

        // 默认设置起点
        double startLongitude = getIntent().getDoubleExtra("startLongitude", 0);
        double startLatitude = getIntent().getDoubleExtra("startLatitude", 0);
        if (startLatitude != 0) {
            locationBean.setStartLatitude(startLatitude);
            locationBean.setStartLongitude(startLongitude);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_navigation_start_btn:// 开始导航
                Intent intentNaviShow = new Intent(this, MapNaviShowActivity.class);
                if (locationBean.isStartNull()) {
                    Toast.makeText(this, "起点为空", Toast.LENGTH_SHORT).show();
                    double startLongitude = getIntent().getDoubleExtra("startLongitude", 0);
                    double startLatitude = getIntent().getDoubleExtra("startLatitude", 0);
                    if (startLatitude != 0) {
                        locationBean.setStartLatitude(startLatitude);
                        locationBean.setStartLongitude(startLongitude);
                    }else {
                        return;
                    }
                }else {
                    intentNaviShow.putExtra("startLongitude", locationBean.getStartLongitude());
                    intentNaviShow.putExtra("startLatitude", locationBean.getStartLatitude());
                }
                if (locationBean.isEndNull()) {
                    Toast.makeText(this, "终点不能为空", Toast.LENGTH_SHORT).show();
                    return;// 终点为空时不继续执行页面跳转
                }else {
                    intentNaviShow.putExtra("endLongitude", locationBean.getEndLongitude());
                    intentNaviShow.putExtra("endLatitude", locationBean.getEndLatitude());
                }
                startActivity(intentNaviShow);
                break;
            case R.id.navigation_start_poi:// Poi搜索起点
                Intent intentNaviPoi = new Intent(this, MapNaviPoiActivity.class);
                startActivityForResult(intentNaviPoi, START);
                break;
            case R.id.navigation_end_poi:// Poi搜索终点
                Intent intent = new Intent(this, MapNaviPoiActivity.class);
                startActivityForResult(intent, END);
                break;
        }
    }

    // 设置strategy选项
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.navi_option_congestion://
                buttonView.setChecked(isChecked);
                mNaviUtil.setCongestion(isChecked);
                break;
            case R.id.navi_option_avoidhightspeed:
                if (buttonView.isChecked()) {
                    if (hightspeed.isChecked()) {
                        Toast.makeText(this, "优先高速与不走高速冲突", Toast.LENGTH_SHORT).show();
                        hightspeed.setChecked(false);
                        mNaviUtil.setHightspeed(false);
                        mNaviUtil.setAvoidhightspeed(isChecked);
                    } else {
                        mNaviUtil.setAvoidhightspeed(true);
                    }
                }
                break;
            case R.id.navi_option_cost:
                buttonView.setChecked(isChecked);
                mNaviUtil.setCost(buttonView.isChecked());
                break;
            case R.id.navi_option_hight_speed:
                if (buttonView.isChecked()) {
                    if (avoidhightspeed.isChecked()) {
                        Toast.makeText(this, "优先高速与不走高速冲突", Toast.LENGTH_SHORT).show();
                        avoidhightspeed.setChecked(false);
                        mNaviUtil.setAvoidhightspeed(false);
                        mNaviUtil.setHightspeed(true);
                    } else {
                        mNaviUtil.setHightspeed(true);
                    }
                }
                break;
        }
    }

    // 返回poi搜索结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case START:
                if (data != null) {
                    double sLongitude = data.getDoubleExtra("longitude", 0);
                    double sLatitude = data.getDoubleExtra("latitude", 0);
                    mStartLongitudeEdt.setText(String.format("%s", sLongitude));
                    mStartLatitudeEdt.setText(String.format("%s", sLatitude));
                    locationBean.setStartLongitude(sLongitude);
                    locationBean.setStartLatitude(sLatitude);
                }
                break;
            case END:
                if (data != null) {
                    double eLongitude = data.getDoubleExtra("longitude", 0);
                    double eLatitude = data.getDoubleExtra("latitude", 0);
                    mEndLongitudeEdt.setText(String.format("%s", eLongitude));
                    mEndLatitudeEdt.setText(String.format("%s", eLatitude));
                    locationBean.setEndLongitude(eLongitude);
                    locationBean.setEndLatitude(eLatitude);
                    break;
                }
        }
    }

    private class InnerLocationBean{
        private double startLongitude;
        private double startLatitude;
        private double endLongitude;
        private double endLatitude;
        private boolean isStartNull = true;
        private boolean isEndNull = true;

        public void setStartLongitude(double startLongitude) {
            this.startLongitude = startLongitude;
            mStartLongitudeEdt.setText(String.valueOf(startLongitude));
        }

        public void setStartLatitude(double startLatitude) {
            this.startLatitude = startLatitude;
            mStartLatitudeEdt.setText(String.valueOf(startLatitude));
        }

        public void setEndLongitude(double endLongitude) {
            this.endLongitude = endLongitude;
            mEndLongitudeEdt.setText(String.valueOf(endLongitude));
        }

        public void setEndLatitude(double endLatitude) {
            this.endLatitude = endLatitude;
            mEndLatitudeEdt.setText(String.valueOf(endLatitude));
        }

        public double getStartLongitude() {
            return startLongitude;
        }

        public double getStartLatitude() {
            return startLatitude;
        }

        public double getEndLongitude() {
            return endLongitude;
        }

        public double getEndLatitude() {
            return endLatitude;
        }

        public boolean isStartNull() {
            return startLongitude == 0 || startLatitude == 0;
        }

        public boolean isEndNull() {
            return endLongitude == 0 || endLatitude == 0;
        }
    }
}
