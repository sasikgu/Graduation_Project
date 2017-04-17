package com.liner.graduationproject.map.utils;

import android.content.Context;

import com.amap.api.navi.model.NaviLatLng;
import com.liner.graduationproject.map.beans.PoiBean;

import java.util.ArrayList;

/**
 *  导航工具类
 */

public class NavigationUtil{

    private NaviLatLng startLatlng;
    private NaviLatLng endLatlng;



    public NavigationUtil() {
    }

//------------------------------------------strategy设置-----------------------------------------------
    private boolean isCongestion;
    private boolean isAvoidhightspeed;
    private boolean isCost;
    private boolean isHightspeed;
    public boolean isCongestion() {
        return isCongestion;
    }
    public boolean isAvoidhightspeed() {
        return isAvoidhightspeed;
    }
    public void setAvoidhightspeed(boolean avoidhightspeed) {
        isAvoidhightspeed = avoidhightspeed;
    }
    public boolean isCost() {
        return isCost;
    }
    public void setCost(boolean cost) {
        isCost = cost;
    }
    public boolean isHightspeed() {
        return isHightspeed;
    }
    public void setHightspeed(boolean hightspeed) {
        isHightspeed = hightspeed;
    }
    public void setCongestion(boolean congestion) {
        isCongestion = congestion;
    }

//----------------------------------获取经纬度，返回起点坐标--------------------------------------
    public NaviLatLng getStartLatlng(double longitude, double latitude){
        return startLatlng = new NaviLatLng(latitude, longitude);
    }

    public NaviLatLng getEndLatlng(double longitude, double latitude){
        return startLatlng = new NaviLatLng(latitude, longitude);
    }
}
