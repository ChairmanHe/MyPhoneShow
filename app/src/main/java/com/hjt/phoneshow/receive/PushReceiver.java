package com.hjt.phoneshow.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hjt.phoneshow.app.MyApp;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
//这是一个极光推送的广播接收器
//极光推送简单的实现步骤

/**
 * 1.去官网下载sdk并且注册AppKey，将下载的sdk的jar文件和so文件分别拷到项目libs下面
 * 2.拷贝清单文件里面的权限和application里面的配置文件
 * 3.初application里面初始化极光推送
 * JPushInterface.setDebugMode(true);
 * JPushInterface.init(this);
 * 4.写一个广播接收器去接收极光推送的广播，这里的广播需去相应的位置注册
 * 5.在极光推送官网后台推送相应的消息或者通知
 */

public class PushReceiver extends BroadcastReceiver {

	private static final String TAG = "PushReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction());

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("收到了自定义消息。消息内容是："
					+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			//这里拿到数据后   发送了一个广播给语音页面，用来播报收到的消息内容
			Intent intent2= new Intent();
			intent2.setAction("jiguang");
	        intent2.putExtra("result",bundle.getString(JPushInterface.EXTRA_MESSAGE));
			MyApp.getInstance().sendBroadcast(intent2);
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			System.out.println("收到了通知");
			// 在这里可以做些统计，或者做些其他工作
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			System.out.println("用户点击打开了通知");
			// 在这里可以自己写代码去定义用户点击后的行为
			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			System.out.println("附加信息:" + extra);

			try {
				JSONObject jo = new JSONObject(extra);
				String url = jo.getString("url");

				System.out.println("url:" + url);
				// 跳浏览器加载网页
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

}
