package com.liner.graduationproject.map.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/12.
 */

public class ViewPagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabs;

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        tabs = new ArrayList<>();
    }

    public void updateData(List<String> tabs, List<Fragment> fragments){
        if (tabs != null && fragments != null) {
            this.fragments.clear();
            this.tabs.clear();
            this.fragments.addAll(fragments);
            this.tabs.addAll(tabs);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return fragments != null && fragments.size() > 0 ? fragments.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (tabs != null) {
            return tabs.get(position);
        }else {
            return "2333";
        }
    }
}
