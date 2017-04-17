package com.liner.graduationproject.map.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.liner.graduationproject.map.beans.PoiBean;

import java.util.ArrayList;

/**
 *
 */

public class PoiSearchUtil implements PoiSearch.OnPoiSearchListener {

    private int itemPosition;// 关键字搜索返回的条目数
    private int itemCount = 10;// 每一页返回结果数量

    // Poi搜索
    private PoiSearch.Query query;

    private Context context;
    private PoiResult mPoiResult;

    public PoiSearchUtil(Context context, PoiDataCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }


    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    // 开始Poi关键字搜索
//    public void startPoiSearch(String cityName, String ctgr, double l, double l2){
//        query = new PoiSearch.Query("餐饮服务","北京");
//        if (itemPosition < 10) {
//            query.setPageSize(10);// 每页显示10条
//        }else {
//            query.setPageNum(itemCount);
//        }
//        query.setPageNum(0);// 查询页码
//        // 初始化PoiSearch对象
//        PoiSearch poiSearch = new PoiSearch(context, query);
//        // 设置周边搜索
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(l,l2), 1000));
//        // 设置监听
//        poiSearch.setOnPoiSearchListener(this);
//        // 发送请求
//        poiSearch.searchPOIAsyn();
//    }

    public void startPoiSearch(String targetName){
        this.startPoiSearch("", targetName);
    }

    public void startPoiSearch(String cityName, String targetName){
        // 如果城市未填写，将城市设置为当前位置 TODO 未实现
        if (cityName == null || cityName.length() == 0) {

        }
        if (targetName != null) {
            query = new PoiSearch.Query(targetName, cityName);
            if (itemPosition < 10) {
                query.setPageSize(10);// 每页显示10条
            }else {
                query.setPageNum(itemCount);
            }
            query.setPageNum(0);// 查询页码
            PoiSearch poiSearch = new PoiSearch(context, query);

            // 设置监听
            poiSearch.setOnPoiSearchListener(this);
            // 发送请求
            poiSearch.searchPOIAsyn();
        }else {
            Toast.makeText(context, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    // Poi关键字搜索返回的数据
    private ArrayList<PoiBean> data;
    // Poi关键字搜索返回数据的接口
    public interface PoiDataCallBack{
        void poiBeanCallBack(ArrayList<PoiBean> data);
    }
    public PoiDataCallBack callBack;

    // Poi搜索返回结果
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 响应码为1000，返回结果成功
            if (result.getQuery().equals(query)) {
                mPoiResult = result;
                if (data == null) {
                    data = new ArrayList<>();
                }else {
                    data.clear();
                }
                ArrayList<PoiItem> poiList = mPoiResult.getPois();
                for (PoiItem poiItem : poiList) {
                    String title = poiItem.getTitle();
                    String cityName = poiItem.getCityName();
                    String adName = poiItem.getAdName();
                    String typeDes = poiItem.getTypeDes();
                    String snippet = poiItem.getSnippet();
                    LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                    double longitude = latLonPoint.getLongitude();
                    double latitude = latLonPoint.getLatitude();
                    PoiBean poiBean = new PoiBean();
                    // 存入地址
                    poiBean.setAddress(cityName+adName+snippet);
                    // 城市名
                    poiBean.setCityName(cityName);
                    // 标题
                    poiBean.setTitle(title);
                    // 分类
                    poiBean.setTypeDes(typeDes);
                    // 经度
                    poiBean.setLongitude(longitude);
                    // 维度
                    poiBean.setLatitude(latitude);
                    data.add(poiBean);
                }
                itemPosition = data.size();
                // Poi关键字搜索的数据回调接口
                callBack.poiBeanCallBack(data);
            }
        }else {
            Toast.makeText(context, "返回结果失败", Toast.LENGTH_SHORT).show();
        }
    }

    // Poi搜索返回状态
    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
