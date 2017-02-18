package com.hjt.phoneshow.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharePreference封装
 * 
 * @author Kevin
 */
public class PrefUtils {
	/**
	 * 两个参数，第一个参数是preferece的名称(比如：MyPref),第二个参数是打开的方式（一般选择private方式）
	 */
	public static final String PREF_NAME = "config";

	public static boolean getBoolean(Context ctx, String key, boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public static void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
}
