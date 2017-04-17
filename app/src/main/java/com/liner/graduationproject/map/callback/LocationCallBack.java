package com.liner.graduationproject.map.callback;

import com.amap.api.location.AMapLocation;

/**
 * Created by Administrator on 2017/4/15.
 */

public interface LocationCallBack {
    void onLocationSuccess(AMapLocation aMapLocation);
    void onLocationFaild(AMapLocation aMapLocation);
}
