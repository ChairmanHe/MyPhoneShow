package com.hjt.phoneshow.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.hjt.phoneshow.R;
import com.hjt.phoneshow.bean.ModelList;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24.
 */

public class MainActivityAdapter extends RecyclerView.Adapter {
    int position;
    private LayoutInflater mInflater;
    private ArrayList<ModelList.List> mlist;
    private Context context;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public MainActivityAdapter(Context context, ArrayList<ModelList.List> list) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        if(list!=null){
            this.mlist = list;
        }else {
            list= new ArrayList<ModelList.List>();
            this.mlist=mlist;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_activity_adapter, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view, mItemClickListener,
                mItemLongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ModelList.List model = mlist.get(position);
            ((ItemViewHolder) holder).name.setText(model.getName());
            ((ItemViewHolder) holder).tip.setText(model.getTips());
            Glide.with(context).load(model.getIcon()).into(new GlideDrawableImageViewTarget(((ItemViewHolder) holder).imageView){

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    ((ItemViewHolder) holder).progressBarMain.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onStart() {
                    super.onStart();
                    ((ItemViewHolder) holder).progressBarMain.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private  ProgressBar progressBarMain;
        private MyItemClickListener mItemClickListener;
        private MyItemLongClickListener mItemLongClickListener;
        TextView name;
        TextView tip;
        ImageView imageView;

        public ItemViewHolder(View itemView, MyItemClickListener listener,
                              MyItemLongClickListener longClickListener) {
            super(itemView);
            imageView = (ImageView)
                    itemView.findViewById(R.id.iv_main_item);
            name = (TextView) itemView.findViewById(R.id.model_name);
            tip = (TextView) itemView.findViewById(R.id.model_tips);
            progressBarMain=(ProgressBar)itemView.findViewById(R.id.small_progress_main);
            // 设置监听器
            this.mItemClickListener = listener;
            this.mItemLongClickListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        // 注册接口监听器
        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClick(v, getPosition());
            }
            return true;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }

    /**
     * 设置接口
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    // 定义接口
    public interface MyItemClickListener {
        public void onItemClick(View view, int position);
    }

    public interface MyItemLongClickListener {
        public boolean onItemLongClick(View view, int position);
    }


}
