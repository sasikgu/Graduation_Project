package com.rock.teachlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rock on 2017/2/20.
 */

public abstract class TeachBaseAdapter<T> extends BaseAdapter {

    private List<T> data;

    private LayoutInflater inflater;

    private int layoutResId;

    public TeachBaseAdapter(Context context,List<T> data,int layoutResId){
        inflater = LayoutInflater.from(context);
        this.layoutResId = layoutResId;
        if (data != null) {
            this.data = data;
        }else{
            this.data = new ArrayList<>();
        }
    }

    public void updateRes(List<T> data){
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addRes(List<T> data){
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         *  数据加载
         *      ① View
         *      ② 数据
         */
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(layoutResId,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 数据加载

        bindData(holder,getItem(position),position);

        return convertView;
    }

    protected abstract void bindData(ViewHolder holder, T item, int position);

    public static class ViewHolder{

        public View itemView;
        // 使用一个Map来对已经实例化过的View做一个缓存
        private Map<Integer,View> cacheViews;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            cacheViews = new HashMap<>();
        }
        // 根据id去convertView中查找实例
        public View findView(int resId){
            // 直接去缓存中查找
            View v = null;
            if (cacheViews.containsKey(resId)) {
                v = cacheViews.get(resId);
            }else{
                v = itemView.findViewById(resId);
                cacheViews.put(resId,v);
            }
            return v;
        }

        public void setText(int resId,String text){
            TextView textView = (TextView) findView(resId);
            textView.setText(text);
        }

    }

}
