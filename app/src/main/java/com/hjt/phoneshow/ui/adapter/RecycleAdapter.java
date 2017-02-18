package com.hjt.phoneshow.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hjt.phoneshow.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/24.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ItemViewHolder> {
    private LayoutInflater mInflater;
    private  ArrayList<String> data;
    private Context context;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;

    public RecycleAdapter(Context context, ArrayList<String> data) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        if(data!=null){
            this.data=data;
        }else{
            data=new ArrayList<String>();
            this.data=data;
        }

    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.fragment_item,null);
//        ItemViewHolder holder = new ItemViewHolder(view, mItemClickListener,
//                mItemLongClickListener);
//        return holder;
//    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_item,parent,false);
        ItemViewHolder holder = new ItemViewHolder(view, mItemClickListener,
                mItemLongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String url_image = data.get(position);
        if(url_image!=null){
            Glide.with(context).load(data.get(position)).placeholder(R.mipmap.zhanweitu).animate(R.anim.my_alpha).into(((ItemViewHolder) holder).imageView);
        }else {
            Glide.with(context).load(R.mipmap.ic_fail).into(((ItemViewHolder)holder).imageView);
        }
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof ItemViewHolder) {
//            Glide.with(context).load(data.get(position)).into((ItemViewHolder) holder.imageView);
//          //  Glide.with(context).load(Uri.parse(data.get(position))).into(((ItemViewHolder) holder).imageView);
//        }
//    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        private MyItemClickListener mItemClickListener;
        private MyItemLongClickListener mItemLongClickListener;
        ImageView imageView;

        public ItemViewHolder(View itemView, MyItemClickListener listener,
                              MyItemLongClickListener longClickListener) {
            super(itemView);
            imageView = (ImageView)
                    itemView.findViewById(R.id.iv_fg_item);
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
