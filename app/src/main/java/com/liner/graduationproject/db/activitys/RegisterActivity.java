package com.liner.graduationproject.db.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liner.graduationproject.BaseApp;
import com.liner.graduationproject.R;
import com.liner.graduationproject.db.bean.DaoSession;
import com.liner.graduationproject.db.bean.User;
import com.liner.graduationproject.db.bean.UserDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText mUserName;
    private EditText mPassword;
    private EditText mAge;
    private EditText mSex;
    private EditText mBirthday;
    private EditText mStar;
    private EditText mPlace;
    private EditText mIntro;
    private Button mRegister;
    private Button mCancle;

    private String userName;
    private String password;
    private String age;
    private String sex;
    private String birthday;
    private String star;
    private String place;
    private String introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {

        mUserName = (EditText) findViewById(R.id.register_username);
        mPassword = (EditText) findViewById(R.id.register_password);
        mAge = (EditText) findViewById(R.id.register_age);
        mSex = (EditText) findViewById(R.id.register_sex);
        mBirthday = (EditText) findViewById(R.id.register_birthday);
        mStar = (EditText) findViewById(R.id.register_star);
        mPlace = (EditText) findViewById(R.id.register_place);
        mIntro = (EditText) findViewById(R.id.register_intro);
        mRegister = (Button) findViewById(R.id.register_register);
        mCancle = (Button) findViewById(R.id.register_cancle);
        mRegister.setOnClickListener(this);
        mCancle.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_register:
                userName = mUserName.getText().toString().trim();
                password = mPassword.getText().toString().trim();
                age = mAge.getText().toString().trim();
                sex = mSex.getText().toString().trim();
                birthday = mBirthday.getText().toString().trim();
                star = mStar.getText().toString().trim();
                place = mPlace.getText().toString().trim();
                introduction = mIntro.getText().toString().trim();
                //判断是否有EditText空着
                if (userName.length() == 0 || password.length() == 0 || age.length() == 0 || sex.length() == 0 || birthday.length() == 0 || star.length() == 0 || place.length() == 0 || introduction.length() == 0) {
                    Log.e(TAG, "onClick: ");
                    Toast.makeText(this, "请将信息填写完全", Toast.LENGTH_SHORT).show();
                } else if (queryTable()){
                    Toast.makeText(this, "该用户名已被注册", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                    insertUser();
                    finish();
                }
                break;
            case R.id.register_cancle:
                finish();
                break;
        }
    }

    private void insertUser() {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setAge(age);
        user.setSex(sex);
        user.setBirthday(birthday);
        user.setConstellation(star);
        user.setResidence(place);
        user.setIntroduce(introduction);

        //获取全局的daoSession
        DaoSession daoSession = ((BaseApp) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        long insert = userDao.insert(user);
        Log.e(TAG, "insertUser: 1表示成功插入" + insert);
    }

    //判断数据库中是否是相同名称的数据
    private boolean queryTable() {
        DaoSession daoSession = ((BaseApp) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        Query<User> query = userDao.queryBuilder()
                .where(UserDao.Properties.UserName.eq(userName))
                .build();
        List<User> list = query.list();
        for (User userList : list) {
            if (userList.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }
}
