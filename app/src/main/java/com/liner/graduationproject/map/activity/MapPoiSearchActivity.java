package com.liner.graduationproject.map.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.liner.graduationproject.MainActivity;
import com.liner.graduationproject.R;
import com.liner.graduationproject.map.BaseActivity;
import com.liner.graduationproject.map.BaseRecyclerAdapter;
import com.liner.graduationproject.map.adapters.RecyclerItemAdapter;
import com.liner.graduationproject.map.beans.PoiBean;
import com.liner.graduationproject.map.utils.PoiSearchUtil;

import java.util.ArrayList;

/**
 *  POI搜索的Activity
 */
public class MapPoiSearchActivity extends BaseActivity implements View.OnClickListener, PoiSearchUtil.PoiDataCallBack, BaseRecyclerAdapter.OnItemClickListener<PoiBean> {

    private EditText mSearch;
    private RecyclerView mRecycler;
    private Button mStartSearch;
    private ImageView mBack;
    private LinearLayoutManager layoutManager;
    private RecyclerItemAdapter adapter;
    private PoiSearchUtil poiSearchUtil;

    @Override
    public int getLayoutId() {
        return R.layout.activity_map_poi_search;
    }

    @Override
    protected void initView() {
        // 初始化文本输入框
        mSearch = (EditText) findViewById(R.id.activity_poi_search_edt);
        mSearch.setHint(getIntent().getStringExtra("hint"));
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (mSearch.getText().length()>0) {
                    mStartSearch.setVisibility(View.VISIBLE);
                    mRecycler.setVisibility(View.GONE);
                }else {
                    mStartSearch.setVisibility(View.GONE);
                    mRecycler.setVisibility(View.VISIBLE);
                }
            }
        });

        // 初始化搜索与回退按钮
        mStartSearch = (Button) findViewById(R.id.activity_poi_search_submit);
        mBack = (ImageView) findViewById(R.id.activity_poi_search_back);
        mStartSearch.setOnClickListener(this);
        mBack.setOnClickListener(this);

        // 初始化RecyclerView
        mRecycler = (RecyclerView) findViewById(R.id.activity_poi_search_recycler);
        layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        adapter = new RecyclerItemAdapter(this);
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        // 初始化Poi搜索工具类
        poiSearchUtil = new PoiSearchUtil(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_poi_search_back:// 点击回退按钮结束Activity
                finish();
                break;
            case R.id.activity_poi_search_submit:
                // 开始搜索
                String targetName = mSearch.getText().toString().trim();
                if (targetName.equals("")) {
                    Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String city = getIntent().getStringExtra("city");
                poiSearchUtil.startPoiSearch(city, targetName);
                mRecycler.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void poiBeanCallBack(ArrayList<PoiBean> data) {
        adapter.updateData(data);
    }

    @Override
    public void onItemClick(int position, PoiBean data) {
        // 获取选中条目的坐标
        double longitude = data.getLongitude();
        double latitude = data.getLatitude();
        // 将坐标传入MapFragment
        Intent intent = new Intent(this, MainActivity.class);
//        Intent intent = getIntent();
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
//        setResult(9, intent);
        startActivity(intent);
        finish();
    }
}
