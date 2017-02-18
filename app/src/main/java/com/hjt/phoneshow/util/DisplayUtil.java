package com.hjt.phoneshow.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.hjt.phoneshow.app.MyApp;


public class DisplayUtil {
	/**
	 * 获取屏幕的宽度
	 *
	 * @return int
	 */
	public static int getScreenWidth() {
		WindowManager manager = (WindowManager) MyApp.getInstance()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 获取屏幕的高度
	 *
	 * @return int
	 */
	public static int getScreenHeight() {
		WindowManager manager = (WindowManager) MyApp.getInstance()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 获取屏幕的密度
	 *
	 * @return
	 */
	public static float getScreenDensity() {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) MyApp.getInstance()
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 1.0f;
	}

	 /**
     * 将 px 转换为 dip 或 dp， 保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {

        /* density 是屏幕比例因子， 以 160dpi（1px = 1dp） 为标准 density 值为1，320dpi（2px = 1dp） 中 density 值为 2（320/160） */
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 將 dip 或 dp 转换为 px, 保证尺寸大小不变
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将 px 转换为 sp， 保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {

        /* scaledDensity，字体的比例因子，类似 density， 会根据用户偏好返回不同的值*/
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将 sp 转换为 px， 保证尺寸大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int sp2px(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue * fontScale + 0.5f);
    }

    /**
     * 使用系统工具类 TypedValue 帮助把 数值 转换到 px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
