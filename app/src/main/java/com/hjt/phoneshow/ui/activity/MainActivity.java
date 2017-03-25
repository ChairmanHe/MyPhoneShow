package com.hjt.phoneshow.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjt.phoneshow.R;
import com.hjt.phoneshow.base.BaseActivity;
import com.hjt.phoneshow.bean.ModelList;
import com.hjt.phoneshow.constant.Constant;
import com.hjt.phoneshow.ui.adapter.MainActivityAdapter;
import com.hjt.phoneshow.ui.presenter.imp.MainActivityPresenter;
import com.hjt.phoneshow.ui.view.IMainActivityView;
import com.hjt.phoneshow.util.CacheUtil;
import com.hjt.phoneshow.util.Util;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements IMainActivityView, PullLoadMoreRecyclerView.PullLoadMoreListener, MainActivityAdapter.MyItemClickListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    //    @BindView(R.id.app_barlayout)
//    AppBarLayout appBarLayout;
    @BindView(R.id.rv_display)
    PullLoadMoreRecyclerView rvDisplay;
    /**
     * 没有网络要显示的布局
     */
    @BindView(R.id.fl_recycleview)
    FrameLayout flRecycleview;
    /**
     * 没有网络状态点击textview刷新
     */
    @BindView(R.id.tv_refresh_nonet)
    TextView tvRefreshNonet;
    @BindView(R.id.fl_nonet)
    FrameLayout flNonet;
    /**
     * 左边侧滑的布局
     */
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    private int pageType = 0;
    private String paramId = "0";
    private ArrayList<ModelList.List> list;
    private MainActivityPresenter presenter;
    private MainActivityAdapter adapter;
    /**
     * 这里用来设置一个点击的时间
     **/
    private long firstClickTime = 0;
    private Snackbar snackbar;

    @Override
    protected void initParams() {
//        //用第三方的申请权限
//        AndPermission.with(this).requestCode(101).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.INTERNET).send();
        //初始化数据
        initData();
        //初始化控件
        initView();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        // 只需要调用这一句，剩下的AndPermission自动完成。
//        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }
//
//    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
//    @PermissionYes(100)
//    private void getLocationYes() {
//        // 申请权限成功，可以去做点什么了。
//        initView();
//
//    }
//
//    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
//    @PermissionNo(100)
//    private void getLocationNo() {
//        // 申请权限失败，可以提醒一下用户。
//        AndPermission.with(this)
//                .requestCode(101)
//                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.INTERNET)
//                .send();
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initData() {
        list = new ArrayList<ModelList.List>();
        //初始化presenter
        presenter = new MainActivityPresenter(this);
    }

    public void initView() {
        //初始化toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /****************************设置DrawerLayout  这里一定要设置不然会报错****************************************/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        /**********************************************************************************************************/
        //设置沉浸式
        //StatusBarUtil.setTranslucentForDrawerLayout(MainActivity.this, drawerLayout);
        //设置NavigationView（左边布局的点击监听）
        navigationView.setNavigationItemSelectedListener(this);
        //获取头部的布局
        View headerView = navigationView.getHeaderView(0);
        ImageView headIv = (ImageView) headerView.findViewById(R.id.head_iv);
        headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后是一个Snackbar
                final Snackbar snackbar = Snackbar.make(view, "哈哈，还没有添加美女图片呢！！", Snackbar
                        .LENGTH_LONG);
                snackbar.show();
                snackbar.setAction("想看请点击", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
            }
        });

        //初始化reclyview
        rvDisplay.setGridLayout(2);//参数为列数
        adapter = new MainActivityAdapter(this, list);
        rvDisplay.setAdapter(adapter);
        //设置上拉加载的背景
        rvDisplay.setFooterViewBackgroundColor(R.color.write);
        //设置下拉刷新的颜色
        rvDisplay.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_blue_dark);
        rvDisplay.setRefreshing(true);
        //设置上拉刷新下拉加载
        rvDisplay.setOnPullLoadMoreListener(this);
        //点击item跳转的监听
        adapter.setOnItemClickListener(this);
        // 点击没有网络的布局刷新
        tvRefreshNonet.setOnClickListener(this);
        //检查权限（注意！！！这里检查权限的时候，后面的逻辑代码尽量不要太复杂，因为权限的回调方法会继续执行后面的代码）
        if (checkPermission())
            return;
        //读取缓存中的数据
        String cache = CacheUtil.getModel(paramId, pageType);
        //读取缓存没有并且没有网络
        if (TextUtils.isEmpty(cache) && Util.getNetworkType() == Util.NetworkType.NONE) {
            noWaln(Constant.NONETANDNOCACHE);
            return;
        } else {
            presenter.getData(pageType, paramId);
        }
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //这里就是网络不好的时候list报下表越界  直接刷新第一页
                if (list.size() == 0 || list == null) {
                    presenter.getData(1, "0");
                } else {
                    pageType = 1;
                    paramId = list.get(0).getId();
                    presenter.getData(pageType, paramId);
                }

            }
        }, 1000);
    }

    //上拉加载
    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageType = 0;
                paramId = list.get(list.size() - 1).getId();
                presenter.getData(pageType, paramId);
            }
        }, 1000);

    }


    @Override
    public void onItemClick(View view, int position) {
        //防止连续点击item
        long currentTime = System.currentTimeMillis();
        if (currentTime - firstClickTime < 800) {
            firstClickTime = currentTime;
            return;
        }
        firstClickTime = currentTime;
        ModelList.List model = list.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("id", model.getId());
        intent.putExtra("name", model.getName());
        intent.putExtra("icon", model.getIcon());
        //startActivity(intent);
        //t添加activity的协作动画
        ActivityOptionsCompat compat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                        view, getString(R.string.transition));
        ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());
    }

    @Override
    public void onClick(View view) {
        flRecycleview.setVisibility(View.VISIBLE);
        flNonet.setVisibility(View.GONE);
        rvDisplay.setRefreshing(true);
        if (Util.getNetworkType() != Util.NetworkType.NONE) {
            if (list.size() == 0) {
                presenter.getData(0, "0");
                return;
            }
            pageType = 1;
            paramId = list.get(0).getId();
            presenter.getData(pageType, paramId);
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.after_net_refresh), Toast.LENGTH_SHORT).show();
            setNoWalnLayout();
        }

    }

    @Override
    public void getDataSuccess(ModelList data) {
        rvDisplay.setPullLoadMoreCompleted();
        success(data);
    }

    @Override
    public void getDataFail(String msg) {
        rvDisplay.setPullLoadMoreCompleted();
        Toast.makeText(MainActivity.this, getResources().getString(R.string.fail_nodata), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getfinish() {
        rvDisplay.setPullLoadMoreCompleted();
    }

    @Override
    public void noWaln(int msg) {
        switch (msg) {
            case Constant.NONETANDNOCACHE:
                setNoWalnLayout();
                break;
            case Constant.NONETANDHASCACHE:
                //Toast.makeText(MainActivity.this, getResources().getString(R.string.no_net), Toast.LENGTH_SHORT).show();
                snackbar = Snackbar.make(flRecycleview, getResources().getString(R.string.no_net), Snackbar
                        .LENGTH_LONG);
                snackbar.show();
                snackbar.setActionTextColor(Color.WHITE);
                //内容的字体颜色与大小
                View mView = snackbar.getView();
                TextView tvSnackbarText = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(80,0,30,0);//4个参数按顺序分别是左上右下
//                tvSnackbarText.setLayoutParams(layoutParams);
                tvSnackbarText.setTextColor(Color.RED);
//                tvSnackbarText.setGravity(Gravity.CENTER);
                snackbar.setAction(getResources().getString(R.string.setting_net), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
                        snackbar.dismiss();
                    }
                });
                rvDisplay.setPullLoadMoreCompleted();
                break;
        }

    }

    //没有网络
    private void setNoWalnLayout() {
        rvDisplay.setPullLoadMoreCompleted();
        if (flNonet.getVisibility() != View.VISIBLE) {
            flNonet.setVisibility(View.VISIBLE);
            flRecycleview.setVisibility(View.GONE);
        }
    }


    private void success(ModelList modelList) {
        if (modelList == null) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.nomore_data), Toast.LENGTH_SHORT).show();
            getfinish();
            return;
        }
        ArrayList<ModelList.Data> d = modelList.getData();
        if (d == null || d.size() == 0) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.nomore_data), Toast.LENGTH_SHORT).show();
            getfinish();
            return;
        }
        ArrayList<ModelList.List> m = d.get(0).getList();
        if (m.size() != 0) {
            if (pageType == 1) {
                list.addAll(0, m);
            } else {
                list.addAll(m);
            }
            if (m.size() < Constant.PAGE_COUNT) {
                pageType = 1;
            }
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.nomore_data), Toast.LENGTH_SHORT).show();
        }
        getfinish();
    }

    //检查权限
    private boolean checkPermission() {
        boolean premission = Util.checkPremission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.WRITE_EXTERNAL_STORAGE);
        if (premission) {
            return true;
        }

        boolean premission2 = Util.checkPremission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, Constant.READ_EXTERNAL_STORAGE);
        if (premission2) {
            return true;
        }
        boolean premission3 = Util.checkPremission(MainActivity.this, Manifest.permission.INTERNET, Constant.INTERNET);
        if (premission3) {
            return true;
        }
        return false;
    }

    //检查权限时的回调
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.WRITE_EXTERNAL_STORAGE);
                    } else {
                        //请求数据
                        presenter.getData(pageType, paramId);
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                break;
            case Constant.READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.READ_EXTERNAL_STORAGE);
                        return;
                    } else {
                        //请求数据
                        //presenter.getData(pageType, paramId);
                    }

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                break;
            case Constant.INTERNET:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, Constant.INTERNET);
                        return;
                    } else {
                        //请求数据
                        //presenter.getData(pageType, paramId);
                    }

                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, getString(R.string.permission_denied), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //快速上滑到最顶端
    public void upTop(View view) {
        if (list == null || list.size() == 0) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.top_nodata), Toast.LENGTH_SHORT).show();
        } else {
            rvDisplay.scrollToTop();
            Toast.makeText(MainActivity.this, getResources().getString(R.string.top), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 这个是侧滑菜单Navigation的点击事件的监听实现方法
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, BaiduMapActivity.class));
        } else if (id == R.id.nav_favorite) {
            startActivity(new Intent(this, VoiceActivity.class));
        } else if (id == R.id.nav_followers) {
            startActivity(new Intent(this, LitePalActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, PullupActivity.class));
        } else if (id == R.id.nav_style) {
            startActivity(new Intent(this, TestActivity.class));
        } else if (id == R.id.downorup) {
            startActivity(new Intent(this, DownOrUpActivity.class));
        } else if (id == R.id.one) {
            startActivity(new Intent(this, WebviewActivity.class));
        } else if (id == R.id.tow) {
            //startActivity(new Intent(this, TestActivity.class));
        } else if (id == R.id.three) {
            //startActivity(new Intent(this, TestActivity.class));
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "分享", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_feedback) {
            Toast.makeText(this, "意见反馈", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.night) {
            //设置夜间模式
            SharedPreferences sp = getSharedPreferences("hjt", this.MODE_PRIVATE);
            boolean isNight = sp.getBoolean("night", false);
            if (isNight) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                sp.edit().putBoolean("night", false).commit();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                sp.edit().putBoolean("night", true).commit();
            }
            recreate();
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //重写返回键  将侧滑菜单隐藏
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //点击搜索的监听事件
    public void search(View view) {
        Intent intent = new Intent(MainActivity.this, SeachActivity.class);
        Bundle bundle = new Bundle();
        //传递对象集合的方式
        bundle.putSerializable("data", (Serializable) list);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //连续按两次退出程序
    private long lastBackTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 判断按下的键是否是“返回”键
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            // 判断当前时间与上次按“返回”键的时间差是否在1秒之内
            if(System.currentTimeMillis() - lastBackTime < 1000) {
                // 返回，且不消费，即让系统按照默认方式处理
                return super.onKeyDown(keyCode, event);
            }
            // 记录下当前按下“返回”键的时间
            lastBackTime = System.currentTimeMillis();
            // 提示
            Toast.makeText(this, "再按一次退出应用程序！", Toast.LENGTH_SHORT).show();
            // 消费
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
