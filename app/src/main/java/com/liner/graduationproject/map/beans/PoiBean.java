package com.liner.graduationproject.map.beans;

/**
 *
 */

public class PoiBean {

    // 城市名称 cityName
    private String cityName;

    // 标题 title
    private String title;

    // 地址 cityName + adName + snippet
    private String address;

    // 分类 typeDes
    private String typeDes;

    // 经度
    private double longitude;

    // 纬度
    private double latitude;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypeDes() {
        return typeDes;
    }

    public void setTypeDes(String typeDes) {
        this.typeDes = typeDes;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
