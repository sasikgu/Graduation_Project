package com.liner.graduationproject.map.fragments;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.liner.graduationproject.R;
import com.liner.graduationproject.map.BaseFragment;
import com.liner.graduationproject.map.adapters.RecyclerItemAdapter;
import com.liner.graduationproject.map.beans.PoiBean;
import com.liner.graduationproject.map.utils.PoiSearchUtil;

import java.util.ArrayList;

/**
 *  POI兴趣点搜索
 */

public class MapPoiFragment extends BaseFragment implements View.OnClickListener, PoiSearchUtil.PoiDataCallBack, SwipeRefreshLayout.OnRefreshListener {

    private EditText mTargetName;
    private EditText mCityName;
    private PoiSearchUtil poiSearchUtil;
    private RecyclerView mRecycler;
    private RecyclerItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected int getResLayout() {
        return R.layout.map_poi_view;
    }

    @Override
    protected void initView() {
        mCityName = ((EditText) layout.findViewById(R.id.fragment_poi_city));// 所在城市
        mTargetName = ((EditText) layout.findViewById(R.id.fragment_poi_target));// 搜索目标
        layout.findViewById(R.id.fragment_poi_submit).setOnClickListener(this);// 搜索按钮

        poiSearchUtil = new PoiSearchUtil(getContext(), this);

        mRecycler = ((RecyclerView) layout.findViewById(R.id.fragment_poi_content_recycler));
        layoutManager = new LinearLayoutManager(getContext());
        mRecycler.setLayoutManager(layoutManager);
        adapter = new RecyclerItemAdapter(getContext());
        mRecycler.setAdapter(adapter);
        mRefreshLayout = ((SwipeRefreshLayout) layout.findViewById(R.id.fragment_poi_refresh_layout));
        mRefreshLayout.setOnRefreshListener(this);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int childCount = recyclerView.getChildCount();
                int itemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (itemCount - childCount > firstVisibleItemPosition) {
                    if (poiSearchUtil.getItemPosition() > poiSearchUtil.getItemCount()) {
                        String cityName = mCityName.getText().toString();
                        String targetName = mTargetName.getText().toString();
                        poiSearchUtil.setItemCount(poiSearchUtil.getItemCount()+10);
                        poiSearchUtil.startPoiSearch(cityName, targetName);
                    }else {
                        Toast.makeText(getContext(), "已经加载完全部内容了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_poi_submit:
                // 开始POI搜索
                String cityName = mCityName.getText().toString();
                String targetName = mTargetName.getText().toString();
                poiSearchUtil.startPoiSearch(cityName, targetName);
                break;
        }
    }

    @Override
    public void poiBeanCallBack(ArrayList<PoiBean> data) {
        adapter.updateData(data);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        String cityName = mCityName.getText().toString();
        String targetName = mTargetName.getText().toString();
        poiSearchUtil.startPoiSearch(cityName, targetName);
    }
}
