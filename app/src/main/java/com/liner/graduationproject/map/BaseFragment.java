package com.liner.graduationproject.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 */

public abstract class BaseFragment extends Fragment {

    protected View layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(getResLayout(), container, false);
        initView();
        return layout;
    }

    protected abstract int getResLayout();

    protected abstract void initView();

}
