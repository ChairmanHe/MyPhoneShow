package com.hjt.phoneshow.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hjt.phoneshow.R;
import com.hjt.phoneshow.bean.NewsBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/30.
 */

public class LitePalAdapter extends RecyclerView.Adapter<LitePalAdapter.LiteViewHolder> {

    private ArrayList<NewsBean> datas;
    private Context context;

    public LitePalAdapter(ArrayList<NewsBean> datas, Context context) {
        if (datas != null) {
            this.datas = datas;
        }
        this.context = context;
    }

    @Override
    public LiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_litepal_item, parent, false);
        return new LiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiteViewHolder holder, int position) {
        NewsBean newsBean = datas.get(position);
        Glide.with(context).load(newsBean.getImgsrc()).into(holder.imageView);
        holder.textView.setText(newsBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class LiteViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public LiteViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.litepal_item_img);
            textView = (TextView) itemView.findViewById(R.id.litepal_item_title);
        }
    }


}
