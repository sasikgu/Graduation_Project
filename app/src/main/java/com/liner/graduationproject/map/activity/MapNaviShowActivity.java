package com.liner.graduationproject.map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.OverviewButtonView;
import com.liner.graduationproject.R;
import com.liner.graduationproject.map.BaseNavigationActivity;
import com.liner.graduationproject.map.utils.LocationUtil;
import com.liner.graduationproject.map.utils.NavigationUtil;

/**
 *  导航页面
 */
public class MapNaviShowActivity extends BaseNavigationActivity  {

    private NavigationUtil mNaviUtil;
    private OverviewButtonView mOverviewButtonView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);

        mNaviUtil = new NavigationUtil();
        mAMapNaviView = (AMapNaviView)findViewById(R.id.activity_show_basic_map);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        // 初始化AMapNavi对象，并设置监听
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        Intent intent = getIntent();
        if (intent != null) {// 接受地址信息
            double startLongitude = intent.getDoubleExtra("startLongitude", 0);
            double startLatitude = intent.getDoubleExtra("startLatitude", 0);
            if (startLongitude != 0 && startLatitude != 0) {// 起点不为空
                mStartLatLng = new NaviLatLng(startLatitude, startLongitude);
            }
            double endLongitude = intent.getDoubleExtra("endLongitude", 0);
            double endLatitude = intent.getDoubleExtra("endLatitude", 0);
            if (endLongitude != 0 && endLatitude != 0) {
                mEndLatLng = new NaviLatLng(endLatitude, endLongitude);
            }
        }
        mOverviewButtonView = (OverviewButtonView) findViewById(R.id.activity_show_overview_btn);
        //设置布局完全不可见
        AMapNaviViewOptions options = mAMapNaviView.getViewOptions();
        options.setLayoutVisible(false);
        mAMapNaviView.setViewOptions(options);

        mAMapNaviView.setLazyOverviewButtonView(mOverviewButtonView);
    }

    // 当 AMapNavi 对象初始化成功后，会进入 onInitNaviSuccess 回调函数，在该回调函数中调用路径规划方法计算路径。
    @Override
    public void onInitNaviSuccess() {
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(mNaviUtil.isCongestion(), mNaviUtil.isAvoidhightspeed(),
                    mNaviUtil.isCost(), mNaviUtil.isHightspeed(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sList.add(mStartLatLng);
        eList.add(mEndLatLng);
        // 回掉函数规划路径
        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);
    }

    @Override
    public void onInitNaviFailure() {
        super.onInitNaviFailure();
        Toast.makeText(this, "InitNaviFailure", Toast.LENGTH_SHORT).show();
    }

    // 当驾车路线规划成功时，若是单一策略，会进 onCalculateRouteSuccess 回调
    @Override
    public void onCalculateRouteSuccess() {
        // 显示路径或开启导航
        // 开始导航
        mAMapNavi.startNavi(NaviType.GPS);
//        mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    // 位置改变
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        super.onLocationChange(aMapNaviLocation);
//        Toast.makeText(this, "当前位置"+aMapNaviLocation.getCoord().getLatitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        super.onCalculateRouteFailure(i);
//        Toast.makeText(this, "fail："+i, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "路线规划失败", Toast.LENGTH_SHORT).show();
    }
}
