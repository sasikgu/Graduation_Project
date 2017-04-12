package com.liner.graduationproject.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/10/010.
 */

public class GuideAdapter extends FragmentPagerAdapter {

    private List<Fragment> data;

    public GuideAdapter(FragmentManager fm, List<Fragment> data) {
        super(fm);
        if (data != null) {
            this.data = data;
        } else {
            this.data = new ArrayList<>();
        }

    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
