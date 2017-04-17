package com.liner.graduationproject.map;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/4/13.
 */

public abstract class BaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeViewCreate();
        setContentView(getLayoutId());
        initView();
    }

    protected void doBeforeViewCreate() {}

    public abstract int getLayoutId();

    protected abstract void initView();

}
