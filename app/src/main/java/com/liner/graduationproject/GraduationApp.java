package com.liner.graduationproject;

import android.app.Application;

import com.rock.teachlibrary.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by 奇思妙想 on 2017/4/10.
 */

public class GraduationApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader.init(this);

        initPicasso();
    }

    private void initPicasso() {

        Picasso picasso = new Picasso.Builder(this)
                //indicatorsEnabled 为显示加载缓存的图片，true默认为显示左上角的标，false默认为不显示左上角的图标
                .indicatorsEnabled(false)
                .build();

        Picasso.setSingletonInstance(picasso);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //内存低的时候，调用垃圾回收器
        System.gc();
    }
}
