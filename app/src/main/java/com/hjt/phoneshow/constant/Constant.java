package com.hjt.phoneshow.constant;

public class Constant {


	public static final String MAXID="max_id";
	public static final String MINID="min_id";
	
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;

	public static final int SPACE_HORIZONTAL=1;
	public static final int SPACE_VERTICAL=2;
	public static final int PAGE_COUNT=4;
	//public static final String GETURL = "http://192.168.16.251/api/json/api.site.json";

	public static final String MODELLIST = "index.php?cl=modelintro&m=modelList";

	public static final String MODELDETAIL = "index.php?cl=modelintro&m=modelDetail";
	/**
	 * 判断既没有网络也没缓存
	 */
	public static final int NONETANDNOCACHE=0;

	/**
	 * 判断没有网络有缓存
	 */
	public static final int NONETANDHASCACHE=1;

   /*************************************网络权限的请求的常量********************************************/
	/**
	 * 写的网络权限
	 */
	public static final int WRITE_EXTERNAL_STORAGE=100;
	/**
	 * 读的网络权限
	 */
	public static final int READ_EXTERNAL_STORAGE=101;
	/**
	 * 网络的权限
	 */
	public static final int INTERNET=102;

}
