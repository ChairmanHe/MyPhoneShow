package com.hjt.phoneshow.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

/**
 * 这是一个判断网络是否存在的工具类
 */
public class NetUtil {

	/**
	 * 判断网络情况
	 * 
	 * @param context
	 *            上下文
	 * @return false 表示没有网络 true 表示有网络
	 */
	public static boolean isNetworkAvalible(Context context) {
		// 获得网络状态管理器
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 建立网络数组
			NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();

			if (net_info != null) {
				for (int i = 0; i < net_info.length; i++) {
					// 判断获得的网络状态是否是处于连接状态
					if (net_info[i].getState() == State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 如果没有网络，则弹出网络设置对话框
	public static void checkNetwork(final Activity activity) {
		if (!NetUtil.isNetworkAvalible(activity)) {
			new AlertDialog.Builder(activity).setTitle("提示").setMessage("设置网络后再使用,是否设置？")
			.setPositiveButton("设置", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
//					((Activity) context).startActivityForResult(
//							new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
					//跳转到wifi设置页面
					(activity).startActivity(new Intent("android.settings.WIFI_SETTINGS"));
				}
			}).setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();

		}
		return;
	}

	/**
	 * 判断网络是否连接
	 **/
	public static boolean netState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取代表联网状态的NetWorkInfo对象
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		// 获取当前的网络连接是否可用
		boolean available = false;
		try {
			available = networkInfo.isAvailable();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (available) {
			Log.i("通知", "当前的网络连接可用");
			return true;
		} else {
			Log.i("通知", "当前的网络连接可用");
			return false;
		}
	}

	/**
	 * 在连接到网络基础之上,判断设备是否是SIM网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean IsMobileNetConnect(Context context) {
		try {
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			if (State.CONNECTED == state)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
