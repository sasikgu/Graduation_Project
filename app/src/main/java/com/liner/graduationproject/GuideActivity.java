package com.liner.graduationproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.liner.graduationproject.adapters.GuideAdapter;
import com.liner.graduationproject.fragments.GuideFragmentOne;
import com.liner.graduationproject.fragments.GuideFragmentThree;
import com.liner.graduationproject.fragments.GuideFragmentTwo;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.guide_vp);

        mViewPager.setAdapter(new GuideAdapter(getSupportFragmentManager(),getData()));


    }

    private List<Fragment> getData() {
        List<Fragment> data = new ArrayList<>();
        data.add(new GuideFragmentOne());
        data.add(new GuideFragmentTwo());
        data.add(new GuideFragmentThree());
        return data;
    }


}
