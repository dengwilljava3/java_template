package com.ay.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加解密工具
 * 
 * @author jackson
 *
 */
public class EncUtil {

	private static final Logger logger = LoggerFactory.getLogger(EncUtil.class);

	public static final String DEFAULT_JOIN = "&";

	public static String HMAC_SHA256(String data, String key) {
		try {

			Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);

			return Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String HMAC_SHA1(String data, String key) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			return new String(byteHMAC);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
		}
		return "";
	}

	public static byte[] HMAC_SHA1By(String data, String key) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
			return byteHMAC;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException ignore) {
		}
		return null;
	}

	public static String hexSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byteO = md[i];
				buf[k++] = hexDigits[byteO >>> 4 & 0xf];
				buf[k++] = hexDigits[byteO & 0xf];
			}
			return new String(buf);
		} catch (NoSuchAlgorithmException e) {
			return null;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 私钥加密过程
	 * 
	 * @param privateKey    私钥
	 * @param plainTextData 明文数据
	 * @return
	 * @throws Exception 加密过程中的异常信息
	 */
	public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
		if (privateKey == null) {
			throw new Exception("加密私钥为空, 请设置");
		}
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此加密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("加密私钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("明文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("明文数据已损坏");
		}
	}

	/**
	 * RSA私钥解密
	 * 
	 * @param str        加密字符串
	 * @param privateKey 私钥
	 * @return 铭文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String decrypt(String str, String privateKey) throws Exception {
		// 64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
		// base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);
		RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
		// RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}

	public static String toMD5(String str) {
		return toMD5(str, 32);
	}

	public static String toMD5(String sourceStr, int length) {
		String result = "";// 通过result返回结果值
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");// 1.初始化MessageDigest信息摘要对象,并指定为MD5不分大小写都可以
			md.update(sourceStr.getBytes());// 2.传入需要计算的字符串更新摘要信息，传入的为字节数组byte[],将字符串转换为字节数组使用getBytes()方法完成
			byte b[] = md.digest();// 3.计算信息摘要digest()方法,返回值为字节数组

			int i;// 定义整型
			// 声明StringBuffer对象
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];// 将首个元素赋值给i
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");// 前面补0
				buf.append(Integer.toHexString(i));// 转换成16进制编码
			}
			result = buf.toString();// 转换成字符串
			return length == 32 ? result : buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;// 返回结果
	}

	public static String encodeStrBase64(String plainText) {
		byte[] b = plainText.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}

	public static String decodeStrBase64(String encodeStr) {
		byte[] b = encodeStr.getBytes();
		Base64 base64 = new Base64();
		b = base64.decode(b);
		String s = new String(b);
		return s;
	}

	public static String encodeBase64(byte[] b) {
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}
	

	public static final String buildSign(Map<String, Object> paramMap, String encryptKey, String encryptValue) {
		return buildSign(paramMap, DEFAULT_JOIN, encryptKey, encryptValue);
	}

	public static final String buildSign(Map<String, Object> paramMap, String splitChar, String encryptKey, String encryptValue) {
		Set<Entry<String, Object>> entrySet = paramMap.entrySet();
		StringBuilder buffer = new StringBuilder();
		for (Entry<String, Object> entry : entrySet) {
			buffer.append(entry.getKey()).append("=").append(entry.getValue()).append(splitChar);
		}
		if (!StringUtil.isNull(encryptKey) && !StringUtil.isNull(encryptValue)) {
			buffer.append(encryptKey).append("=").append(encryptValue);
		} else if (!StringUtil.isNull(encryptValue)) {
			buffer = new StringBuilder(buffer.substring(0, buffer.length() - 1));
			buffer.append(encryptValue);
		} else {
			buffer = new StringBuilder(buffer.substring(0, buffer.length() - 1));
		}
		logger.info("明文 = {}, 密文 = {}", buffer.toString(), EncUtil.toMD5(buffer.toString()));
		return EncUtil.toMD5(buffer.toString());
	}

	public static final String buildSignNoEncrypt(Map<String, Object> paramMap, String encryptKey, String encryptValue) {
		return buildSignNoEncrypt(paramMap, DEFAULT_JOIN, encryptKey, encryptValue);
	}

	public static final String buildSignNoEncrypt(Map<String, Object> paramMap, String splitChar, String encryptKey, String encryptValue) {
		Set<Entry<String, Object>> entrySet = paramMap.entrySet();
		StringBuilder buffer = new StringBuilder();
		for (Entry<String, Object> entry : entrySet) {
			buffer.append(entry.getKey()).append("=").append(entry.getValue()).append(splitChar);
		}
		if (!StringUtil.isNull(encryptKey) && !StringUtil.isNull(encryptValue)) {
			buffer.append(encryptKey).append("=").append(encryptValue);
		} else if (!StringUtil.isNull(encryptValue)) {
			buffer = new StringBuilder(buffer.substring(0, buffer.length() - 1));
			buffer.append(encryptValue);
		} else {
			buffer = new StringBuilder(buffer.substring(0, buffer.length() - 1));
		}
		logger.info("铭文 = {}", buffer.toString());
		return buffer.toString();
	}
}
