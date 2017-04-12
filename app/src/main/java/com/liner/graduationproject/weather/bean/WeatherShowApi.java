package com.liner.graduationproject.weather.bean;

import java.util.List;

/**
 * Created by 奇思妙想 on 2017/4/10.
 */

public class WeatherShowApi {

    private String area;
    private String areaid;
    private List<WeatherdayList> dayList;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public List<WeatherdayList> getDayList() {
        return dayList;
    }

    public void setDayList(List<WeatherdayList> dayList) {
        this.dayList = dayList;
    }
}
