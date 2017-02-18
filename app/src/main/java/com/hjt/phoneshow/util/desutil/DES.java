package com.hjt.phoneshow.util.desutil;

import android.util.Base64;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DES {
	/**
	 * 屏蔽部分为需要第三方的jar包
	 */

	private byte[] desKey;

	public DES(String desKey) {
		this.desKey = desKey.getBytes();
	}

	private byte[] desEncrypt(byte[] plainText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(desKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte encryptedData[] = cipher.doFinal(plainText);
		return encryptedData;
	}

	private byte[] desDecrypt(byte[] encryptText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(desKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte decryptedData[] = cipher.doFinal(encryptText);
		return decryptedData;
	}

	public String encrypt(String input) throws Exception {
		return URLEncoder.encode(base64Encode(desEncrypt(input.getBytes())),
				"UTF-8");
	}

	public String decrypt(String input) throws Exception {
		byte[] result = base64Decode(URLDecoder.decode(input, "UTF-8"));
		return new String(desDecrypt(result));
	}

	private static String base64Encode(byte[] s) {
		if (s == null)
			return null;
		return Base64.encodeToString(s, Base64.DEFAULT); 
	}

	private static byte[] base64Decode(String s) throws IOException {
		if (s == null)
			return null;
		return Base64.decode(s, Base64.DEFAULT); 
	}
}
