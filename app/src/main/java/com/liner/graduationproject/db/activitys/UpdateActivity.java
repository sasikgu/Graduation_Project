package com.liner.graduationproject.db.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.liner.graduationproject.BaseApp;
import com.liner.graduationproject.R;
import com.liner.graduationproject.db.bean.DaoSession;
import com.liner.graduationproject.db.bean.User;
import com.liner.graduationproject.db.bean.UserDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    private static final String TAG = UpdateActivity.class.getSimpleName();
    private String updateUserName;
    private EditText mUserName;
    private EditText mPw;
    private EditText mAge;
    private EditText mSex;
    private EditText mBirthday;
    private EditText mStar;
    private EditText mPlace;
    private EditText mIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
    }

    private void initView() {
        //从上个界面传过来的用户名
        updateUserName = getIntent().getStringExtra("updateUserName");
        Log.e(TAG, "initView: " + updateUserName);


        mUserName = (EditText) findViewById(R.id.update_username);
        mPw = (EditText) findViewById(R.id.update_password);
        mAge = (EditText) findViewById(R.id.update_age);
        mSex = (EditText) findViewById(R.id.update_sex);
        mBirthday = (EditText) findViewById(R.id.update_birthday);
        mStar = (EditText) findViewById(R.id.update_star);
        mPlace = (EditText) findViewById(R.id.update_place);
        mIntro = (EditText) findViewById(R.id.update_intro);

        queryMsg();


    }

    private void queryMsg() {
        DaoSession daoSession = ((BaseApp) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        Query<User> query = userDao.queryBuilder()
                .where(UserDao.Properties.UserName.eq(updateUserName))
                .build();
        List<User> list = query.list();
        for (User user : list) {
            mUserName.setText(user.getUserName());
            mPw.setText(user.getPassword());
            mAge.setText(user.getAge());
            mSex.setText(user.getSex());
            mBirthday.setText(user.getBirthday());
            mStar.setText(user.getConstellation());
            mPlace.setText(user.getResidence());
            mIntro.setText(user.getIntroduce());
        }
    }

    private void update(){
        //先查询，再修改数据，更新到数据库中
        DaoSession daoSession = ((BaseApp) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        Query<User> build = userDao.queryBuilder()
                .where(UserDao.Properties.UserName.eq(updateUserName))
                .build();

        List<User> list = build.list();
        //更新数据
        for (User user : list) {
            user.setUserName(mUserName.getText().toString().trim());
            user.setPassword(mPw.getText().toString().trim());
            user.setAge(mAge.getText().toString().trim());
            user.setSex(mSex.getText().toString().trim());
            user.setBirthday(mBirthday.getText().toString().trim());
            user.setConstellation(mStar.getText().toString().trim());
            user.setResidence(mPlace.getText().toString().trim());
            user.setIntroduce(mIntro.getText().toString().trim());
        }
        Toast.makeText(this, "数据更新成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("updateUserName",mUserName.getText().toString().trim());
        setResult(RESULT_OK,intent);
        finish();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_ok:
                update();
                break;
            case R.id.update_cancle:
                finish();
                break;
        }

    }
}
