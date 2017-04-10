package com.liner.graduationproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.liner.graduationproject.fragments.MapFragment;
import com.liner.graduationproject.fragments.WeatherFragment;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    private DrawerLayout mDrawer;
    private Toolbar mToolbar;

    /**
     * 当前正在显示的Fragment的记录
     */
    private Fragment mShowFragment;

    private View mIndicator;
    private FrameLayout mTitlePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        initView();
    }

    private void initView() {
        mDrawer = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //默认一进界面就打开菜单
        mDrawer.openDrawer(Gravity.LEFT);
        mDrawer.addDrawerListener(this);
        setSupportActionBar(mToolbar);
        //Toolbar与DrawerLayout 的联动特效
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer);
        //想要随着DrawerLayout滑动进行改变，需要监听DrawerLayout的滑动
        mDrawer.addDrawerListener(actionBarDrawerToggle);
        //需要调用开发者同步
        actionBarDrawerToggle.syncState();

        //默认进去就是地图界面
        switchPage(MapFragment.class);

        mIndicator = (View) findViewById(R.id.main_title_indicator);
        mTitlePage = (FrameLayout) findViewById(R.id.main_title_page);


    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_title_map:
                switchPage(MapFragment.class);
                moveTitleIndicator(0);
                break;
            case R.id.main_title_weather:
                switchPage(WeatherFragment.class);
                moveTitleIndicator(1);
                break;
        }


    }

    private void switchPage(Class<? extends Fragment> cls){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        // 开始进入隐藏操作
        if (mShowFragment != null) {
            transaction.hide(mShowFragment);
        }
        // 显示我们将要显示的页面
        mShowFragment = fm.findFragmentByTag(cls.getName());
        if (mShowFragment != null) {
            transaction.show(mShowFragment);
        } else {
            try {
                mShowFragment = cls.newInstance();
                transaction.add(R.id.main_content, mShowFragment, cls.getName());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        transaction.commit();
    }


    //offset 0-1
    public void moveTitleIndicator(float offset){
        int width = mTitlePage.getWidth();
        mIndicator.setTranslationX(offset*width/2);

    }
}
