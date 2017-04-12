package com.liner.graduationproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.liner.graduationproject.db.activitys.LoginActivity;
import com.liner.graduationproject.db.bean.DaoSession;
import com.liner.graduationproject.db.bean.User;
import com.liner.graduationproject.db.bean.UserDao;
import com.liner.graduationproject.fragments.MapFragment;
import com.liner.graduationproject.fragments.WeatherFragment;

import org.greenrobot.greendao.query.Query;
import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * 当前正在显示的Fragment的记录
     */
    private Fragment mShowFragment;

    private View mIndicator;
    private FrameLayout mTitlePage;
    private View mCover;

    //从登录界面传过来的值
    private String loginUserName;
    private TextView menuUserName;
    private TextView menuAge;
    private TextView menuSex;
    private TextView menuBirthday;
    private TextView menuStar;
    private TextView menuPlace;
    private TextView menuIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initView();
    }

    private void initView() {
        //菜单栏
        mCover = (View) findViewById(R.id.main_cover);
        //标题栏的下划线
        mIndicator = (View) findViewById(R.id.main_title_indicator);
        //标题栏
        mTitlePage = (FrameLayout) findViewById(R.id.main_title_page);

        //默认一进app就加载地图界面
        switchPage(MapFragment.class);

        loginUserName = getIntent().getStringExtra("loginUserName");
//        if (!loginUserName.equals("")) {
//            showMsg();
//        }
        menuUserName = (TextView) findViewById(R.id.username);
        menuAge = (TextView) findViewById(R.id.age);
        menuSex = (TextView) findViewById(R.id.sex);
        menuBirthday = (TextView) findViewById(R.id.birthday);
        menuStar = (TextView) findViewById(R.id.constellation);
        menuPlace = (TextView) findViewById(R.id.location);
        menuIntroduction = (TextView) findViewById(R.id.introduction);

    }


    public void onClick(View view) {
        switch (view.getId()) {
            //点击地图按钮
            case R.id.main_title_map:
                switchPage(MapFragment.class);
                moveTitleIndicator(0);
                break;
            //点击天气按钮
            case R.id.main_title_weather:
                switchPage(WeatherFragment.class);
                moveTitleIndicator(1);
                break;
            //点击左上角图标，显示菜单
            case R.id.main_content_image_btn:
                showCover();
                break;
            //点击菜单左上角图标，隐藏菜单
            case R.id.main_cover_image_btn:
                hideCover();
                break;
            //点击头像图标跳转到登录注册界面
            case R.id.user_icon:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent,110);
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
                transaction.add(R.id.main_content_container, mShowFragment, cls.getName());
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

    //隐藏菜单
    private void hideCover(){
        if (mCover.getVisibility() == View.VISIBLE){
            mCover.setVisibility(View.GONE);
        }
    }
    private void showCover(){
        mCover.setVisibility(View.VISIBLE);

    }

    private void showMsg() {
        DaoSession daoSession = ((BaseApp) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        Query<User> query = userDao.queryBuilder()
                .where(UserDao.Properties.UserName.eq(loginUserName))
                .build();
        List<User> list = query.list();
        for (User userList : list) {
            if (userList.getUserName().equals(loginUserName)){
                menuUserName.setText(userList.getUserName());
                menuAge.setText(userList.getAge());
                menuSex.setText(userList.getSex());
                menuBirthday.setText(userList.getBirthday());
                menuStar.setText(userList.getConstellation());
                menuPlace.setText(userList.getResidence());
                menuIntroduction.setText(userList.getIntroduce());
            }
        }

    }


    //登录成功之后，回传回来的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 110:
                if (resultCode == RESULT_OK) {
                    String loginUserName = data.getStringExtra("loginUserName");
                    Log.e(TAG, "onActivityResult: "+loginUserName );
                }
                break;
        }
    }
}
