package com.hjt.phoneshow.app;

import android.app.Service;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatDelegate;

import com.baidu.mapapi.SDKInitializer;
import com.hjt.phoneshow.listener.NetworkConnectivityListener;
import com.hjt.phoneshow.service.LocationService;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;


public class MyApp extends LitePalApplication {

    private static final String DB_NAME = "weibo.db";

    private static  MyApp mContext;
    public LocationService locationService;
    public Vibrator mVibrator;
    // 保存网络状态
    private int netType;
    /**
     * 网络的监听
     */
    private NetworkConnectivityListener mNetChangeReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        mContext =this;
        //初始化夜间模式
        initNightMode();
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        // 1.启动网络监听   这个是实时监听  并且可以在没有网络的情况下弹出设置框
        mNetChangeReceiver = new NetworkConnectivityListener();
        mNetChangeReceiver.startListening(this);
//        //初始化全局异常捕获处理类
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(getApplicationContext());
        //初始化极光推送服务
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    public static MyApp getInstance(){
        return mContext;
    }
    private void initNightMode() {
        SharedPreferences sp = this.getSharedPreferences("hjt", this.MODE_PRIVATE);
        boolean isNight = sp.getBoolean("night", false);
        if (isNight) {
            //使用夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    // 2.在app中保存网络监听的状态

    public NetworkConnectivityListener getNetworkListener() {
        return mNetChangeReceiver;
    }
    public int getNetType() {
        return netType;
    }
    public void setNetType(int netType) {
        this.netType = netType;
    }
    // public void onDestroy() {
    // // 3.退出程序的时候取消网络监听
    // mNetChangeReceiver.stopListening();
    // android.os.Process.killProcess(android.os.Process.myPid());
    // }
}
