package com.hjt.phoneshow.util.desutil;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesCbc {
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	public static final String DES = "DES";

	public static byte[] desKey;

	public DesCbc(String key) {
		desKey = key.getBytes();
	}

	/**
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public String encrypt(String str) throws Exception {
		return URLEncoder.encode(base64Encode(desEncrypt(str.getBytes())),
				"UTF-8");
	}

	private byte[] desEncrypt(byte[] plainText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(desKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec iv = new IvParameterSpec(desKey);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv, sr);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	private byte[] desDecrypt(byte[] encryptText) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(desKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
		IvParameterSpec iv = new IvParameterSpec(desKey);
		cipher.init(Cipher.DECRYPT_MODE, key, iv, sr);
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	private String base64Encode(byte[] s) {
		if (s == null)
			return null;
		return Base64.encodeToString(s, Base64.DEFAULT);
	}

	private byte[] base64Decode(String s) throws IOException {
		if (s == null)
			return null;
		return Base64.decode(s, Base64.DEFAULT);
	}

	public String decrypt(String input) throws Exception {
		byte[] result = base64Decode(URLDecoder.decode(input, "UTF-8"));
		return new String(desDecrypt(result));
	}
}
