package com.rock.teachlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rock on 2017/2/27.
 */

public abstract class TeachMultiTypeBaseAdapter<T> extends BaseAdapter {

    private List<T> data;

    private int[] layoutIds;

    private LayoutInflater inflater;
    // 使用可变参数 作为布局数量进行传入
    public TeachMultiTypeBaseAdapter(Context context,List<T> data,int... layoutIds){
        inflater = LayoutInflater.from(context);
        if (data != null) {
            this.data = data;
        }else{
            this.data = new ArrayList<>();
        }
        this.layoutIds = layoutIds;
    }

    public void addRes(List<T> data){
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void updateRes(List<T> data){
        if (data != null) {
            this.data.clear();
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
    public int getViewTypeCount() {
        return layoutIds.length;
    }

    /**
     *   type 必须从 0开始，0,1,2,3...
     *
     *   根据位置获取对应元素的View的类型
     *   ① 获取对应位置的对象
     *   ② 获取对象的type属性
     */
    @Override
    public int getItemViewType(int position) {
        int type = 0;
        // 获取对应位置的对象
        T t = getItem(position);
        // 获取 t对象的class
        Class<?> cls = t.getClass();
        // 获取指定对象中的type字段
        try {
            Field field = cls.getDeclaredField("type");
            // 添加访问权限
            field.setAccessible(true);
            // 从指定对象中 获取指定字段的值
            type = field.getInt(t);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        getItemViewType(position)  返回值是int ，0 ，1,2，...
        if (convertView == null) {
            convertView = inflater.inflate(layoutIds[getItemViewType(position)],parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        // 下面开始进行数据加载 ① View的持有zhe ② 数据 ③ 位置 算是预留
        bindData(holder,getItem(position),position);

        return convertView;
    }

    public abstract void bindData(ViewHolder holder, T item, int position);

    public static class ViewHolder{

        public View itemView;

        private Map<Integer,View> cacheViews;

        public ViewHolder(View itemView){
            this.itemView = itemView;
            cacheViews = new HashMap<>();
        }

        public View findView(int resId){
            View view = null;
            if (cacheViews.containsKey(resId)) {
                view = cacheViews.get(resId);
            }else{
                view = itemView.findViewById(resId);
                cacheViews.put(resId,view);
            }
            return view;
        }

        public void setText(int resId,String content){
            ((TextView) findView(resId)).setText(content);
        }



    }

}
