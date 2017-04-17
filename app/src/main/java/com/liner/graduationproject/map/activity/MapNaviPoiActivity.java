package com.liner.graduationproject.map.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.liner.graduationproject.R;
import com.liner.graduationproject.map.BaseRecyclerAdapter;
import com.liner.graduationproject.map.adapters.RecyclerItemAdapter;
import com.liner.graduationproject.map.beans.PoiBean;
import com.liner.graduationproject.map.utils.PoiSearchUtil;

import java.util.ArrayList;

public class MapNaviPoiActivity extends AppCompatActivity implements View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener<PoiBean>,PoiSearchUtil.PoiDataCallBack {

    private EditText mPoiEdt;
    private RecyclerView mRecycler;
    private RecyclerItemAdapter adapter;
    private PoiSearchUtil poiSearchUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navi_poi);
        poiSearchUtil = new PoiSearchUtil(this, this);
        initView();
    }

    private void initView() {
        ImageView backImg = (ImageView) findViewById(R.id.activity_navi_poi_back);
        backImg.setOnClickListener(this);
        Button searchBtn = (Button) findViewById(R.id.activity_navi_poi_search);
        searchBtn.setOnClickListener(this);
        mPoiEdt = (EditText) findViewById(R.id.activity_navi_poi_edt);
        mRecycler = (RecyclerView) findViewById(R.id.activity_navi_poi_recycler);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layout);
        adapter = new RecyclerItemAdapter(this);
        mRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_navi_poi_back:// 回退按钮
                finish();
                break;
            case R.id.activity_navi_poi_search:// 开始poi搜索
                String path = mPoiEdt.getText().toString().trim();
                if (path.length() > 0) {
                    // 调用搜索方法
                    poiSearchUtil.startPoiSearch(path);
                }else {
                    Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onItemClick(int position, PoiBean data) {
        // 条目点击事件
        double longitude = data.getLongitude();
        double latitude = data.getLatitude();
        String title = data.getTitle();
        String cityName = data.getCityName();
        data.setLongitude(longitude);
        data.setLatitude(latitude);
        data.setTitle(title);
        data.setCityName(cityName);
        Intent intent = getIntent();
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        // 返回intent
        setResult(RESULT_OK, intent);
        finish();
    }

    // 返回poi搜索结果
    @Override
    public void poiBeanCallBack(ArrayList<PoiBean> data) {
        adapter.updateData(data);
    }
}
