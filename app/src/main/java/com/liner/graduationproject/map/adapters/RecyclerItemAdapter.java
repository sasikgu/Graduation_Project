package com.liner.graduationproject.map.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liner.graduationproject.R;
import com.liner.graduationproject.map.BaseRecyclerAdapter;
import com.liner.graduationproject.map.beans.PoiBean;

/**
 *
 */

public class RecyclerItemAdapter extends BaseRecyclerAdapter<PoiBean> {

    public RecyclerItemAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int type) {
        View itemView = inflater.inflate(R.layout.map_poi_item_view, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int pos, PoiBean data) {
        if (viewHolder instanceof Holder) {
            ViewHolder holder = (Holder) viewHolder;
            TextView title = (TextView) holder.findView(R.id.poi_item_title);
            TextView address = (TextView) holder.findView(R.id.poi_item_address);
            TextView type = (TextView) holder.findView(R.id.poi_item_type);
            title.setText(data.getTitle());
            address.setText(data.getAddress());
            type.setText(data.getTypeDes());
        }
    }

    public static class Holder extends ViewHolder{

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
