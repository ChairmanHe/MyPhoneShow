package com.hjt.phoneshow.util.desutil;

import java.security.MessageDigest;

public class DesUtil {
	private static DesUtil desUtil;

	private DES des;
	private DesCbc cbc;

	/**
	 * 秘钥
	 */
	public static String DES = "dab3ico8";
	public static String DES_CBC = "kenxinda";

	private DesUtil() {
		des = new DES(DES);
		String key = getKey();
		cbc = new DesCbc(key);
	}

	public static DesUtil getDesUtil() {
		if (desUtil == null) {
			desUtil = new DesUtil();
		}
		return desUtil;
	}

	public String encrypt(String str) {
		try {
			return des.encrypt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String decrypt(String str) {
		try {
			return des.decrypt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String encrypt2cbc(String str) {
		try {
			return cbc.encrypt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String decrypt2cbc(String str) {
		try {
			return cbc.decrypt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private String getKey() {
		String ser = android.os.Build.SERIAL;
		if (ser == null) {
			ser = "kenxinda";
		}
		String md = md5(ser);
		return md.substring(md.length() - 8);
	}

	/**
	 * MD5加密 生成32位md5�?
	 */
	public String md5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");

			char[] charArray = inStr.toCharArray();
			byte[] byteArray = new byte[charArray.length];

			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuffer hexValue = new StringBuffer();
			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString();
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return "";
	}
}
