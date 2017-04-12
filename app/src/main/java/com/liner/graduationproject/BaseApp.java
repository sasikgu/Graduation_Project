package com.liner.graduationproject;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.liner.graduationproject.db.bean.DaoMaster;
import com.liner.graduationproject.db.bean.DaoSession;
import com.rock.teachlibrary.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/4/11/011.
 */

public class BaseApp extends Application {


    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader.init(this);

        initPicasso();

        //实例化一个OpenHelper
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "usermsg-db");
        //获取一个数据库对象
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        //使用数据库对象构造一个DaoMaster
        DaoMaster daoMaster = new DaoMaster(database);

        //开启DaoSession
        daoSession = daoMaster.newSession();

    }

    public DaoSession getDaoSession(){
        return daoSession;
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
