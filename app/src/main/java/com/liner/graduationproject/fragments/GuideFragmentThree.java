package com.liner.graduationproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liner.graduationproject.MainActivity;
import com.liner.graduationproject.R;

/**
 * Created by Administrator on 2017/4/10/010.
 */

public class GuideFragmentThree extends Fragment implements View.OnClickListener {

    private View inflate;
    private View mBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_guide_three,container,false);
        mBtn = inflate.findViewById(R.id.guide_btn);
        mBtn.setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_btn:
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;

        }
    }
}
