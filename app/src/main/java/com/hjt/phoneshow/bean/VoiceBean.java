package com.hjt.phoneshow.bean;

import java.util.ArrayList;

/**
 * 语音信息封装
 * @author Kevin
 */
//这个是科大讯飞语音用的实体类
public class VoiceBean {

	public ArrayList<WSBean> ws;

	public class WSBean {
		public ArrayList<CWBean> cw;
	}

	public class CWBean {
		public String w;
	}
}
