package com.liner.graduationproject.map.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.liner.graduationproject.map.callback.LocationCallBack;

/**
 * 定位，显示小蓝点
 */

public class LocationUtil implements LocationSource, AMapLocationListener {

    private MapView mMapView;
    private Context context;

    // 回调地址的接口
    private LocationCallBack locationCallBack;

    public void setLocationCallBack(LocationCallBack callBack, boolean isCurrentLocation) {
        locationCallBack = callBack;
        this.isCurrentLocation = isCurrentLocation;
    }

    private boolean isCurrentLocation = true;
    private double longitude;
    private double latitude;


    private int sdkVersion = Build.VERSION.SDK_INT;
    private AMap map;

    // 新的地址信息
    private AMapLocationClientOption mLocationOption;

    public LocationUtil(Context context, MapView mMapView) {
        this.context = context;
        this.mMapView = mMapView;
    }

    // 开启定位
    public void location() {
        map = mMapView.getMap();
        // 实例化UiSetting对象
        UiSettings uiSettings = map.getUiSettings();
        // 设置默认的定位按钮
        uiSettings.setMyLocationButtonEnabled(true);
        // 可触发定位并显示当前位置
        map.setMyLocationEnabled(true);
        // 显示指南针
        uiSettings.setCompassEnabled(true);
        // 显示比例尺
        uiSettings.setScaleControlsEnabled(true);
        // 判断API版本，分别处理事件
        if (sdkVersion > 20) {
            locationMap();
        } else {
            compatLocationMap(mMapView);
        }
    }

    // 正常版(5.0版本后无需依赖定位SDK)
    private void locationMap() {
        // 获取
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        if (!isCurrentLocation) {// 如果不是定位到当前位置
            if (latitude != 0 && longitude != 0) {
                float latitude = (float) this.latitude;
                float longitude = (float) this.longitude;
                myLocationStyle.anchor(latitude, longitude);
            }
        }
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        // 设置定位间隔
//        myLocationStyle.interval(60 * 1000);
        // 设置定位蓝点的style
        map.setMyLocationStyle(myLocationStyle);
        // 启动定位蓝点
        map.setMyLocationEnabled(true);
    }

    // 兼容版(5.0之前，需要依赖定位SDK，设置定位监听)
    private void compatLocationMap(MapView mapView) {
        AMap map = mapView.getMap();
        // 设置改变视角监听
//        map.setOnCameraChangeListener(this);
        // 设置定位监听与显示小蓝点
        map.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        map.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        map.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
    }

    //-------------------------Fragment生命周期的调用方法--------------------------------
    // 启动定位客户端
    public void onResumeClient() {
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.startLocation();
        }
        location();
    }

    public void onPauseClient() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    // 销毁定位客户端
    public void onDestroyClient() {
        if (mLocationClient != null) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stopLocation();
            }
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    //-------------------------------API20之前的定位监听回调方法----------------------------------------------------------------
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;

    // 位置改变
    @Override
    public void activate(OnLocationChangedListener listener) {
        //  位置改变的监听接口
        mListener = listener;
        mLocationClient = new AMapLocationClient(context);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位回调监听
        mLocationClient.setLocationListener(this);
        // 设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
//            // 设置单次定位
        mLocationOption.setOnceLocation(true);
        // 获取最近3s内精度最高的一次定位结果：
        // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);
        // 不是单次定位
        if (!mLocationOption.isOnceLocation()) {
            // 设置定位间隔
            mLocationOption.setHttpTimeOut(60 * 1000);
        }
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mLocationClient.startLocation();//启动定位
    }

    // 停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            if (!mLocationOption.isOnceLocation()) {
                mLocationClient.stopLocation();
            }
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    // 定位SDK定位成功后的回调方法
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                locationCallBack.onLocationSuccess(aMapLocation);
                if (!isCurrentLocation) {// 不是定位到当前位置
                    if (latitude != 0 && longitude != 0) {
                        aMapLocation.setLatitude(latitude);
                        aMapLocation.setLongitude(longitude);
                    }else {
                        Toast.makeText(context, "定位失败", Toast.LENGTH_SHORT).show();
                    }
                }
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                locationCallBack.onLocationFaild(aMapLocation);
                Log.e("AmapErr:", errText);
            }
        }
    }

    public void setLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
