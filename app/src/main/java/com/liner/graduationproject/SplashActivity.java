package com.liner.graduationproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean firstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = this.getSharedPreferences("check", MODE_PRIVATE);
        editor = sp.edit();

        final Intent isToSecond = new Intent(this, GuideActivity.class);
        final Intent isToThree = new Intent(this, MainActivity.class);

        Timer timer = new Timer();
        firstLoadJudge(timer, isToSecond, isToThree);

    }

    private void firstLoadJudge(Timer timer, Intent isToSecond, Intent isToThree) {
        boolean firstLoad = sp.getBoolean("firstLoad", true);
        if (firstLoad) {
            editor.putBoolean("firstLoad", false);
            editor.commit();
            delayToNext(timer, isToSecond);
        } else {
            delayToNext(timer, isToThree);
        }
    }

    private void delayToNext(Timer timer, final Intent intent) {

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                startActivity(intent);

                finish();

            }
        };

        timer.schedule(task, 1000 * 3);

    }


}
