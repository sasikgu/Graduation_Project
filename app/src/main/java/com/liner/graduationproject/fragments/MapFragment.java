package com.liner.graduationproject.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.liner.graduationproject.R;
import com.liner.graduationproject.map.activity.MapNavigationActivity;
import com.liner.graduationproject.map.activity.MapPoiSearchActivity;
import com.liner.graduationproject.map.beans.PoiBean;
import com.liner.graduationproject.map.callback.LocationCallBack;
import com.liner.graduationproject.map.utils.LocationUtil;
import com.liner.graduationproject.map.utils.PoiSearchUtil;

import java.util.ArrayList;

/**
 *
 */

public class MapFragment extends Fragment implements View.OnClickListener, LocationCallBack,
        AMap.OnPOIClickListener, PoiSearchUtil.PoiDataCallBack {

    private static final String TAG = MapFragment.class.getSimpleName();
    private View layout;
    private MapView mMapView;
    private EditText mPoiSearch;
    private Button mNavigation;
    private LocationUtil locationUtil;
    private AMap mMap;
    private double longitude;
    private double latitude;
    private PoiSearchUtil poiSearchUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_map, container, false);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化MapView
        mMapView = ((MapView) layout.findViewById(R.id.map_aMapView));
        mMapView.onCreate(savedInstanceState);
        // 初始化mMap
        mMap = mMapView.getMap();
        // 设置默认缩放级别
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // 设置poi点击事件
        mMap.setOnPOIClickListener(this);

        // 初始化定位工具类
        locationUtil = new LocationUtil(getContext(), mMapView);
        // 初始化Poi搜索工具类 TODO 周边位置搜索，未实现
        poiSearchUtil = new PoiSearchUtil(getContext(), this);

        mPoiSearch = ((EditText) layout.findViewById(R.id.map_search_edt));
        layout.findViewById(R.id.location_current).setOnClickListener(this);
        mNavigation = ((Button) layout.findViewById(R.id.map_navigation));
        mPoiSearch.setOnClickListener(this);
        mNavigation.setOnClickListener(this);

        // 回传Poi搜索到的地址信息
        Intent intent = getActivity().getIntent();
        if (intent.getDoubleExtra("longitude", 0) != 0) {
            longitude = intent.getDoubleExtra("longitude", 0);
            latitude = intent.getDoubleExtra("latitude", 0);
            locationUtil.setLocation(longitude, latitude);
            locationUtil.setLocationCallBack(this, false);
        }else {
            // 设置定位信息的回调接口 默认定位到当前位置
            locationUtil.setLocationCallBack(this, true);
        }
        locationUtil.location();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        locationUtil.onResumeClient();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        locationUtil.onPauseClient();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        locationUtil.onDestroyClient();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map_search_edt:// poi关键字搜索
                Intent intentPoiSearch = new Intent(getActivity(), MapPoiSearchActivity.class);
                intentPoiSearch.putExtra("hint", "搜索");
                intentPoiSearch.putExtra("city", "北京");
                getActivity().startActivity(intentPoiSearch);
//                getActivity().finish();
                // 不是当前位置
                locationUtil.setLocationCallBack(this, false);
                break;
            case R.id.map_navigation:// 导航时传入当前位置 TODO 位置传入失败
                Intent intentNavigation = new Intent(getActivity(), MapNavigationActivity.class);
                intentNavigation.putExtra("startLongitude", longitude);
                intentNavigation.putExtra("startLatitude", latitude);
                getActivity().startActivity(intentNavigation);
                break;
            case R.id.location_current:// 点击左上角定位到当前所在位置
                // 设置当前位置
                locationUtil.setLocationCallBack(this, true);
                // 定位到当前位置
                locationUtil.location();
                break;
        }
    }

    // 定位成功
    @Override
    public void onLocationSuccess(AMapLocation aMapLocation) {
        // 将定位地址作为全局变量存储
        longitude = aMapLocation.getLongitude();
        latitude = aMapLocation.getLatitude();
    }

    // 定位失败
    @Override
    public void onLocationFaild(AMapLocation aMapLocation) {
        int errorCode = aMapLocation.getErrorCode();
        String errorInfo = aMapLocation.getErrorInfo();
        Log.e(TAG, "onLocationFaild: " + errorCode + " errorInfo : " + errorInfo);
    }

    // poi点击事件回调
    @Override
    public void onPOIClick(Poi poi) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(poi.getCoordinate());
        TextView textView = new TextView(getActivity().getApplicationContext());
        textView.setText(poi.getName() + "\n经度: " + (int) poi.getCoordinate().longitude + ", 纬度: " + (int) poi.getCoordinate().latitude);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.drawable.custom_info_bubble);
        markerOptions.icon(BitmapDescriptorFactory.fromView(textView));
        mMap.addMarker(markerOptions);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mMap.clear();
            }
        }).start();
        poiClickCallBack.onClick(poi);
    }

    @Override
    public void poiBeanCallBack(ArrayList<PoiBean> data) {

    }

    //---------------------Poi兴趣点点击回调(方便Activity操作)-----------------------------------------------------
    public interface PoiClickCallBack {
        void onClick(Poi poi);
    }

    private PoiClickCallBack poiClickCallBack;

    public void setPoiClickCallBack(PoiClickCallBack poiClickCallBack) {
        this.poiClickCallBack = poiClickCallBack;
    }

}
