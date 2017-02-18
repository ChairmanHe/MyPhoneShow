package com.hjt.phoneshow.ui.adapter;

import android.support.v4.util.CircularArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseLoadingAdapter;
import com.hjt.phoneshow.bean.DesignItem;

/**
 * Created by Administrator on 2016/12/31.
 */
//这是一个上拉加载下拉刷新的adapter 这里面有瀑布流
public class DesignLoaderMoreAdapter extends BaseLoadingAdapter<DesignItem> {
    private CircularArray<DesignItem> mDesignItems;

    public DesignLoaderMoreAdapter(RecyclerView recyclerView, CircularArray<DesignItem> datas) {
        super(recyclerView, datas);

        mDesignItems = datas;
    }

    //正常条目
    public class DesignViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CardView cardView;

        public DesignViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_design);
            cardView = (CardView) view.findViewById(R.id.cardView_designer);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_design, parent, false);
        return new DesignViewHolder(view);
    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {
        DesignViewHolder viewHolder = (DesignViewHolder) holder;
        DesignItem designItem = mDesignItems.get(position);
        if (position == 10) {
            //设置瀑布流的条目大小
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(260, 360);
            lp.setMargins(10, 40, 10, 80);
            viewHolder.cardView.setLayoutParams(lp);
        }

        viewHolder.textView.setText(designItem.name);
    }

}
