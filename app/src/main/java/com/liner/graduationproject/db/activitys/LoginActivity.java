package com.liner.graduationproject.db.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.liner.graduationproject.BaseApp;
import com.liner.graduationproject.MainActivity;
import com.liner.graduationproject.R;
import com.liner.graduationproject.db.bean.DaoSession;
import com.liner.graduationproject.db.bean.User;
import com.liner.graduationproject.db.bean.UserDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText mLoginUserName;
    private EditText mLoginPw;

    private String loginUserName;
    private String loginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        initView();
    }

    private void initView() {
        mLoginUserName = (EditText) findViewById(R.id.login_username);
        mLoginPw = (EditText) findViewById(R.id.login_password);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                loginUserName = mLoginUserName.getText().toString().trim();
                loginPw = mLoginPw.getText().toString().trim();
                if (queryUser()){
                    Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功之后将数据回传到上一个页面
                    Intent intent = new Intent();
                    intent.putExtra("loginUserName",loginUserName);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    Toast.makeText(this, "账号密码输入错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.login_register_btn:
                //跳转到用户注册界面
                startActivity(new Intent(this,RegisterActivity.class));
                break;

        }


    }


    private boolean queryUser(){
        DaoSession daoSession = ((BaseApp) getApplication()).getDaoSession();
        UserDao userDao = daoSession.getUserDao();
        Query<User> query = userDao.queryBuilder()
                .where(UserDao.Properties.UserName.eq(loginUserName))
                .build();
        List<User> list = query.list();
        for (User userList : list) {
            if (userList.getUserName().equals(loginUserName)) {
                if (userList.getPassword().equals(loginPw)){
                    return true;
                }
            }
        }
        return false;
    }
}
