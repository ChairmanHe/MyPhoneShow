package com.hjt.phoneshow.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.hjt.phoneshow.app.MyApp;

import java.io.File;

/**
 * 这个是手机秀使用的时候使用的
 */
public class Util {
    public enum NetworkType {
        NONE, WIFI, GPRS;
    }

    //	/**
//	 * 设置系统通知栏背景
//	 *
//	 * @param activity
//	 */
//	public static void renderNotificationBar(Activity activity) {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(activity, true);
//			SystemBarTintManager tintManager = new SystemBarTintManager(
//					activity);
//			tintManager.setStatusBarTintEnabled(true);
//			tintManager.setStatusBarTintResource(R.color.ActionBar);
//		}
    //检查系统运行时候的权限
    public static boolean checkPremission(Activity context, String premission, int resultCode) {
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, premission);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, new String[]{premission}, resultCode);
            return true;
        }
        return false;
    }

    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 获取缓存目录
     *
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = MyApp.getInstance().getExternalCacheDir()
                    .getPath();
        } else {
            cachePath = MyApp.getInstance().getCacheDir().getPath();
        }
        File file = new File(cachePath + File.separator + uniqueName);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取App当前版本号
     *
     * @return
     */
    public static int getAppVersion() {
        try {
            PackageInfo info = MyApp.getInstance()
                    .getPackageManager()
                    .getPackageInfo(
                            MyApp.getInstance().getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 是否是第一次使用App
     *
     * @return
     */
    public static boolean isFirstUse() {
        SharedPreferences sp = MyApp.getInstance()
                .getSharedPreferences("isFirstUse", 0);
        boolean isFirstUse = sp.getBoolean("isFirstUse", true);
        if (isFirstUse) {
            sp.edit().putBoolean("isFirstUse", false).commit();
        }
        return isFirstUse;
    }

    /**
     * 获取屏幕宽
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.widthPixels; // ��Ļ�߶ȣ����أ�
        return height;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels; // ��Ļ�߶ȣ����أ�
        return height;
    }

    /**
     * 获取屏幕密度
     *
     * @param activity
     * @return
     */
    public static float getScreenDensity(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;
        return density;
    }

    public static NetworkType getNetworkType() {
        ConnectivityManager manager = (ConnectivityManager) MyApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return NetworkType.NONE;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return NetworkType.NONE;
        } else {
            boolean wifi = manager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting();
            if (wifi) {
                return NetworkType.WIFI;
            } else {
                return NetworkType.GPRS;
            }
        }
    }


}
