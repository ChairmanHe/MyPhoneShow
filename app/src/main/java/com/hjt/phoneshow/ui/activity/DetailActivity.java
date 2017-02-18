package com.hjt.phoneshow.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseActivity;
import com.hjt.phoneshow.bean.Detail;
import com.hjt.phoneshow.listener.AppBarStateChangeListener;
import com.hjt.phoneshow.ui.adapter.ViewPagerAdapter;
import com.hjt.phoneshow.ui.presenter.imp.DetailActivityPresenter;
import com.hjt.phoneshow.ui.view.IDetailActivityView;
import com.hjt.phoneshow.util.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailActivity extends BaseActivity implements IDetailActivityView {
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_tab)
    TabLayout toolbarTab;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.main_vp_container)
    ViewPager mainVpContainer;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.progress_loading)
    ProgressBar progressLoading;

    private String icon;
    private String id_;
    private String name_;
    private DetailActivityPresenter presenter;
    private Detail.Info detailInfo;
    private ArrayList<ArrayList<String>> datas;

    @Override
    protected void initParams() {
        //设置全透明沉浸式
        StatusBarUtil.setTransparent(DetailActivity.this);
        initData();
        //初始化toolbar
        initToolBarAsHome();
        //初始化控件
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.detail_activity;
    }

    private void initData() {
        Intent intent = getIntent();
        id_ = intent.getStringExtra("id");
        name_ = intent.getStringExtra("name");
        icon = intent.getStringExtra("icon");
        //发送请求 获取数据
        presenter = new DetailActivityPresenter(this);
        presenter.getDetailData(id_);
    }

    private void initView() {
        collapsingToolbarLayout.setTitle(name_);
        //给glide添加监听来设置progressbar
        Glide.with(this).load(icon).crossFade().into(new GlideDrawableImageViewTarget(imageview) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                //在这里添加一些图片加载完成的操作
                progressLoading.setVisibility(View.GONE);
            }

            @Override
            public void onStart() {
                super.onStart();
                progressLoading.setVisibility(View.VISIBLE);
            }
        });
        //位Viewpager和TabLayout建立联系
        mainVpContainer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (toolbarTab));
        toolbarTab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (mainVpContainer));
        //设置appBarLayout的滑动监听来改变状态栏的透明度
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    //
                    //StatusBarUtil.setTranslucentForCoordinatorLayout(DetailActivity.this,getResources().getColor(R.color.transparent));
                    //StatusBarUtil.setTransparentForImageView(DetailActivity.this,imageview);
                    StatusBarUtil.setTransparent(DetailActivity.this);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    //StatusBarUtil.setTranslucentForCoordinatorLayout(DetailActivity.this,getResources().getColor(R.color.half_transparent));
                    StatusBarUtil.setTranslucentDiff(DetailActivity.this);
                } else {
                    //中间状态
                }
            }
        });
        // toolbarTab.setupWithViewPager(mainVpContainer);
    }

    @Override
    public void getDataSuccess(Detail data) {
        if (data == null) {
            //加载失败
            //retryLayout.fail();
            return;
        }
        detailInfo = data.getData();
        Log.e("sucess", detailInfo.toString());
        if (detailInfo == null) {
            //加载失败  retryLayout.fail();
            return;
        }
        //  retryLayout.success();
        //初始化数据
        init();
    }

    private void init() {
        // TODO    这里的代码还没有优化
        datas = new ArrayList<ArrayList<String>>();
        datas.add(detailInfo.getBase_img());
        datas.add(detailInfo.getParameter_img());
        datas.add(detailInfo.getSummary_img());

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(this, datas);
        mainVpContainer.setAdapter(vpAdapter);
        mainVpContainer.setCurrentItem(0);
        mainVpContainer.setOffscreenPageLimit(0);
    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void getfinish() {

    }

    @Override
    public void noWaln(int msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (datas != null) {
            datas.clear();
            datas.removeAll(datas);
        }
        if (presenter != null) {
            presenter = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
