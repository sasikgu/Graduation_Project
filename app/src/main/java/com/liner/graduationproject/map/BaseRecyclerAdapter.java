package com.liner.graduationproject.map;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 自带头布局和条目点击事件的RecyclerViewAdapter基类
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 标志位
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;

    // 数据源
    private List<T> data = new ArrayList<>();
    private View mHeaderView;
    private View mFootView;
    private OnItemClickListener mListener;
    protected LayoutInflater inflater;
    protected Context context;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
    }

    // 接口
    public interface OnItemClickListener<T> {
        void onItemClick(int position, T data);
    }

    // 设置条目点击事件
    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    // 设置头视图
    public void setHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    // 获取头视图
    public View getHeaderView() {
        return mHeaderView;
    }

    // 设置足布局
    public void setFootView(View footView) {
        this.mFootView = footView;
    }

    // 获取足布局
    public View getFootView() {
        return mFootView;
    }

    // 更新数据(下拉刷新)
    public void updateData(List<T> data) {
        if (data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    // 添加数据(上拉加载更多)
    public void addData(List<T> data) {
        if (data != null) {
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    // 获取条目type
    @Override
    public int getItemViewType(int position) {
        // 处于最后一项切足布局不为空
        if (mFootView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        if (mHeaderView == null) {// 头布局为空
            return TYPE_NORMAL;
        } else if (position == 0) {// 头布局不为空, 并且是第一条数据
            return TYPE_HEADER;
        }
        // 默认无头布局, 无足布局
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {// 每当存在头/足布局时， 总长度+1
        if (!(data.size() > 0)) {
            return 0;
        }
        int index = 0;
        if (mHeaderView != null) {
            index++;
        }
        if (mFootView != null) {
            index++;
        }
        return data.size() + index;
    }

    // 创建ViewHolder对象
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {//　如果存在头布局且为头布局所在条目
            // 返回头布局
            return new ViewHolder(mHeaderView);
        } else if (mFootView != null && viewType == TYPE_FOOTER) {// 如果足布局不为空且为足布局所在条目
            // 返回足布局
            return new ViewHolder(mFootView);
        }
        // 其余条目
        return onCreate(parent, viewType);
    }

    // 子类抽象方法，创建ViewHolder(new)
    public abstract RecyclerView.ViewHolder onCreate(ViewGroup parent, final int type);

    @Override// 绑定数据(非头布局), 添加了条目点击事件的监听调用(监听不为空)
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        // 判断是正常条目，绑定数据并设置监听
        if (getItemViewType(position) == TYPE_NORMAL) {
            // final方便内部类调用(监听事件)
            final int pos = getRealPosition(viewHolder);// 获取position(排除头布局影响)
            final T data = this.data.get(pos);// 获取数据
            onBind(viewHolder, pos, data);// 调用绑定数据的抽象方法
            // 判断是否有监听事件(添加监听)
            if (mListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(pos, data);
                    }
                });
            }
        }
    }

    // 获取真实position(剔除head影响)
    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        // 如果不存在头布局，返回position，否则-1
        return mHeaderView == null ? position : position - 1;
    }

    // 子类通过此方法绑定数据
    public abstract void onBind(RecyclerView.ViewHolder viewHolder, int pos, T data);

    @Override// 为网格布局设置头/足布局占据整行/列
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 获取布局管理器
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        // 如果是GridLayoutManager, 头布局占据一整行(头布局自动占据整行)
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override// 得到跨度大小
                public int getSpanSize(int position) {
                    // 如果是头布局, 合并单元格, 否则正常显示
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override// 为瀑布流设置头布局占据整行(瀑布流没有setSpanSizeLookup方法，通过布局参数实现占据整行/列效果)
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 获取布局参数
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null// 布局参数不为空
                // 瀑布流
                && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams// 瀑布流的布局参数
                && holder.getLayoutPosition() == 0) {// 是第一项条目(头布局)
            // 向下转型
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            // 设置占据全宽/高
            params.setFullSpan(true);
        }
    }

    // 创建ViewHolder，添加优化
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 添加Map缓存减少findView的次数,　优化ViewHolder
        public HashMap<Integer, View> cacheView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        // 通过布局id查找控件
        public View findView(int resId) {
            View view = null;
            if (cacheView == null) {
                cacheView = new HashMap<>();
            }
            if (cacheView.containsKey(resId)) {
                view = cacheView.get(resId);
            } else {
                view = itemView.findViewById(resId);
                cacheView.put(resId, view);
            }
            return view;
        }
    }
}
