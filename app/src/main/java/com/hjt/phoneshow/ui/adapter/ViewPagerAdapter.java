package com.hjt.phoneshow.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.ui.activity.BigImageActivity;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {
    private  ArrayList<ArrayList<String>> list;
    private LayoutInflater inflate;
    private Context context;
    public ViewPagerAdapter( Context context,ArrayList<ArrayList<String>> list) {
        this.inflate=LayoutInflater.from(context);
        this.context=context;
        if(list!=null){
            this.list=list;
        }else {
            list= new ArrayList<ArrayList<String>>();
            this.list=list;
        }

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
    //在PagerAdapter里的instantiateItem方法中，如果有加载数据的逻辑，则viewpager就会预加载
    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view =inflate.inflate(R.layout.recycleview,null);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<String> data = list.get(position);
        RecycleAdapter adapter= new RecycleAdapter(context,data);
        recyclerView.setAdapter(adapter);
        //设置recyclerView的点击事件
        setAdapterListener(adapter,data);
        container.addView(view);
        return view;
    }

    private void setAdapterListener(RecycleAdapter adapter,final ArrayList<String> data) {
        adapter.setOnItemClickListener(new RecycleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent= new Intent(context,BigImageActivity.class);
                Bundle bundle= new Bundle();
                ArrayList<String> base_img = data;
                bundle.putInt("positon",position);
                bundle.putStringArrayList("base_img",base_img);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }


}