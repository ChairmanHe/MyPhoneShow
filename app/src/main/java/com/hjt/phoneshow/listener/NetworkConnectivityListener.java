package com.hjt.phoneshow.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.hjt.phoneshow.app.MyApp;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 这是网络监听的实现类
 */
public class NetworkConnectivityListener {
	public final static int NET_STATE_UNKNOWN = 0x01;
	public final static int NET_STATE_DISCONNECTED = 0x02;
	public final static int NET_STATE_WIFI_CONNECTED = 0x03;
	public final static int NET_STATE_2G_CONNECTED = 0x04;
	public final static int NET_STATE_3G_CONNECTED = 0x05;
	public final static int NET_STATE_4G_CONNECTED = 0x06;

	private Context context_;

	private HashMap<Handler, Integer> handlers_ = new HashMap<Handler, Integer>();

	private  int state_;

	private boolean isListening_;

	/** Network connectivity information */
	private NetworkInfo networkInfo_;

	private ConnectivityBroadcastReceiver receiver_;

	private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				// 如果是平板电脑就不要取TYPE_MOBILE 因为取出来的是空
				// NetworkInfo mobNetInfo =
				// connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
					state_ = NET_STATE_WIFI_CONNECTED;
				} else {
					state_ = NET_STATE_DISCONNECTED;
				}
				MyApp.getInstance().setNetType(state_);
				NotifyEvent();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * Return general class of network type, such as "3G" or "4G". In cases
	 * where classification is contentious, this method is conservative.
	 */
	private int getNetworkClass(int networkType) {
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return NET_STATE_2G_CONNECTED;
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return NET_STATE_3G_CONNECTED;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return NET_STATE_4G_CONNECTED;
		default:
			return NET_STATE_UNKNOWN;
		}
	}

	public static boolean IsNormalNet() {
		if (MyApp.getInstance().getNetType() == NetworkConnectivityListener.NET_STATE_DISCONNECTED)
			return false;
		else
			return true;
	}

	public static String getNetTypeStr() {
		String s = "";
		switch (MyApp.getInstance().getNetType()) {
		case NET_STATE_WIFI_CONNECTED:
			s = "wifi";
			break;
		case NET_STATE_2G_CONNECTED:
			s = "2G";
			break;
		case NET_STATE_3G_CONNECTED:
			s = "3G";
			break;
		case NET_STATE_DISCONNECTED:
			s = "无网络";
			break;
		default:
			s = "未知";
			break;
		}
		return s;
	}

	private void NotifyEvent() {
		// Notify any handlers.
		Iterator<Handler> it = handlers_.keySet().iterator();
		while (it.hasNext()) {
			Handler target = it.next();
			Message message = Message.obtain(target, handlers_.get(target));
			message.arg1 = state_;
			target.sendMessage(message);
		}
	}

	/**
	 * Create a new NetworkConnectivityListener.
	 */
	public NetworkConnectivityListener() {
		state_ = NET_STATE_UNKNOWN;
		receiver_ = new ConnectivityBroadcastReceiver();
	}

	/**
	 * This method starts listening for network connectivity state changes.
	 * 
	 * @param context
	 */
	public synchronized void startListening(Context context) {
		if (!isListening_) {
			context_ = context;

			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			context.registerReceiver(receiver_, filter);
			isListening_ = true;
		}
	}

	/**
	 * This method stops this class from listening for network changes.
	 */
	public synchronized void stopListening() {
		if (isListening_) {
			context_.unregisterReceiver(receiver_);
			context_ = null;
			networkInfo_ = null;
			isListening_ = false;
		}
	}

	/**
	 * This methods registers a Handler to be called back onto with the
	 * specified what code when the network connectivity state changes.
	 * 
	 * @param target
	 *            The target handler.
	 * @param what
	 *            The what code to be used when posting a message to the
	 *            handler.
	 */
	public void registerHandler(Handler target, int what) {
		handlers_.put(target, what);
	}

	/**
	 * This methods unregisters the specified Handler.
	 * 
	 * @param target
	 */
	public void unregisterHandler(Handler target) {
		handlers_.remove(target);
	}

	public int getState() {
		return state_;
	}

	/**
	 * Return the NetworkInfo associated with the most recent connectivity
	 * event.
	 * 
	 * @return {@code NetworkInfo} for the network that had the most recent
	 *         connectivity event.
	 */
	public NetworkInfo getNetworkInfo() {
		return networkInfo_;
	}

}