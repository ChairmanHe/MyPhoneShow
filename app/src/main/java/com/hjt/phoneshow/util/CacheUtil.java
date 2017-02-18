package com.hjt.phoneshow.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class CacheUtil {
	private static final String MODELNAME = "model";
	private static final String DETAILNAME = "detail";
	static DiskLruCache mDiskLruCache;
	static {
		try {
			File cacheDir = Util.getDiskCacheDir("data");
			mDiskLruCache = DiskLruCache.open(cacheDir, Util.getAppVersion(),
					1, 1 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void fluch() {
		try {
			mDiskLruCache.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			mDiskLruCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveModel(String model, String id, int pageType) {
		try {
			String key = Md5.md5(MODELNAME + id + pageType);
			mDiskLruCache.remove(key);
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				BufferedOutputStream bos = new BufferedOutputStream(
						outputStream);
				bos.write(model.getBytes());
				bos.flush();
				bos.close();
				outputStream.close();
				editor.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getModel(String id, int pageType) {
		String key = Md5.md5(MODELNAME + id + pageType);
		try {
			DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
			if (snapShot != null) {
				return snapShot.getString(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveDetail(String detail, String mid) {
		try {
			String key = Md5.md5(DETAILNAME + mid);
			mDiskLruCache.remove(key);
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				BufferedOutputStream bos = new BufferedOutputStream(
						outputStream);
				bos.write(detail.getBytes());
				bos.flush();
				bos.close();
				outputStream.close();
				editor.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDetail(String mid) {
		String key = Md5.md5(DETAILNAME + mid);
		try {
			DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
			if (snapShot != null) {
				return snapShot.getString(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getBaseUrl() {
		String key = Md5.md5("BaseUrl");
		try {
			DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
			if (snapShot != null) {
				return snapShot.getString(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveBaseUrl(String url) {
		String key = Md5.md5("BaseUrl");
		try {
			DiskLruCache.Editor editor = mDiskLruCache.edit(key);
			if (editor != null) {
				OutputStream outputStream = editor.newOutputStream(0);
				BufferedOutputStream bos = new BufferedOutputStream(
						outputStream);
				bos.write(url.getBytes());
				bos.flush();
				editor.commit();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
