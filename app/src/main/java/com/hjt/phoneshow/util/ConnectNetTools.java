package com.hjt.phoneshow.util;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hjt.phoneshow.listener.NetworkConnectivityListener;

//如果检查没有网络  会弹出对话框  和NetworkConnectivityListener配合使用
public class ConnectNetTools {
	private static ConnectNetTools intance;
	private static Context context;

	private ConnectNetTools() {
	};

	public static ConnectNetTools getIntance(Context context_) {
		context = context_;
		if (intance == null)
			intance = new ConnectNetTools();
		return intance;
	}

	/**
	 * 调用这个方法可以检查网络 如果没有网络 会自动弹出对话框 调到网络设置界面
	 * 
	 * @return
	 */
	public boolean hasNetwork() {
		boolean boo = NetworkConnectivityListener.IsNormalNet();
		if (!boo) 
			new AlertDialog.Builder(context).setTitle("提示").setMessage("设置网络后再使用,是否设置？")
					.setPositiveButton("设置", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
//							((Activity) context).startActivityForResult(
//									new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS), 0);
							//跳转到wifi设置页面
							((Activity) context).startActivity(new Intent("android.settings.WIFI_SETTINGS"));
						}
					}).setNegativeButton("取消", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).show();
		
		return boo;
	}

	// 判断WIFI网络是否可用的方法
	public boolean isOpenWifi() {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

}
