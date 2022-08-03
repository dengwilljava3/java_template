package com.ay.common.util;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.TreeMap;

import com.ay.common.util.Base64;

public class AlipaySignUtil {

	/**
	 * 绛惧悕绠楁硶
	 */
	public static final String SIGN_ALGORITHMS_1 = "SHA1WithRSA";
	public static final String SIGN_ALGORITHMS_256 = "SHA256WithRSA";

	/**
	 * 签名
	 * 
	 * @param paramMap    待签名的信息
	 * @param paramMap
	 * @param privateKey 私钥
	 * @throws Exception
	 */

	public static String signByPrivateKey(Map<String, Object> paramMap, String privateKey, String signType) {

		Object[] strs = (Object[]) paramMap.keySet().toArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			if (i < strs.length - 1) {
				sb.append(strs[i] + "=").append(paramMap.get(strs[i]) + "&");
			} else {
				sb.append(strs[i] + "=").append(paramMap.get(strs[i]));
			}
		}

		String backsign = sign(sb.toString(), privateKey, "utf-8", signType);

		return backsign;
	}

	/**
	 * 验证同步回执信息
	 * 
	 * @param merchantPublicKey 第五方商户设定的公钥
	 * @param value
	 * @param sign
	 * @throws Exception
	 */
	public static boolean validateByPublicKey(String merchantPublicKey, String value, String sign, String signType)
			throws Exception {
		return doCheck(value, sign, merchantPublicKey, "utf-8", signType);

	}

	/**
	 * 验证异步回执信息
	 * 
	 * @param merchantPublicKey 第五方商户设定的公钥
	 * @param treeMap
	 * @param sign
	 * @throws Exception
	 */
	public static boolean validateByPublicKey(String merchantPublicKey, TreeMap<String, String> treeMap, String sign,
			String signType) throws Exception {
		// 验证

		Object[] strs = (Object[]) treeMap.keySet().toArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			if (i < strs.length - 1) {
				sb.append(strs[i] + "=").append(treeMap.get(strs[i]) + "&");
			} else {
				sb.append(strs[i] + "=").append(treeMap.get(strs[i]));
			}
		}
		sign = sign.replace(" ", "+"); // 用java代码发送http请求，会自动把+号变成空格
		return doCheck(sb.toString(), sign, merchantPublicKey, "utf-8", signType);

	}

	/**
	 * RSA签名
	 * 
	 * @param content    待签名数据
	 * @param privateKey 商户私钥
	 * @param encode     字符集编码
	 * @return 签名值
	 */
	private static String sign(String content, String privateKey, String encode, String signType) {
		try {
			System.out.println("content = " + content + ", privateKey = " + privateKey + ", encode = " + encode
					+ ", signType = " + signType);
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode2(privateKey));

			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(signType);

			signature.initSign(priKey);
			signature.update(content.getBytes(encode));

			byte[] signed = signature.sign();

//			return Base64.encode(signed);
			return EncUtil.encodeBase64(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 * 
	 * @param content   待签名数据
	 * @param sign      签名值
	 * @param publicKey 分配给开发商公钥
	 * @param encode    字符集编码
	 * @return 布尔值
	 */
	private static boolean doCheck(String content, String sign, String publicKey, String encode, String signType) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode2(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(signType);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(encode));

			boolean bverify = signature.verify(Base64.decode2(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static void main(String[] args) {
		Map<String, Object> paramMap = new TreeMap<>();
		paramMap.put("a", "123");
		String sign = signByPrivateKey(paramMap , "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALuCe4US4K2UpktApsOHHgUqo1KZkZ6eAwQcWkUHFiD4xZETsIiga0UoAec5qaCSFTtyfR+fTY3rVztz4QyhLEZpbDcJdrQwmZIZ8EWHb8yykqu4+zqGWTS0U24v0ecpS+v/6LDc+1grI0wYY3/jbO5EidCOFbuaJ8lekdGpllOfAgMBAAECgYEAt474lEDyqXpHgIt9tRNkk/I1h/rFzt0oXrHSvJg/VW+zGF6KBnXkDQMpOc38/C+FkXh2fKpaJYApGQ5LVS3CREvuu0uIZqzBt1O+M4h8jJIkifew5/v2ICW0jLG9dAc+gdxM4NkXFXu9EStNH8UclCNtu7J6gPYGS8O9lwnRsQECQQD1FMOJYra7HjkirHmGNCpnlMHA7VrmZhHqyhZ0G7Q6u8BJlyZ2d+eJLiKxPJp/XrnNVcVY1427/yjb4JTc0m8hAkEAw90YtF4rHnXbSEsP5ICtbExYqIIzBJ+5B5s90bq9paB2bkqHdKvTxY6DJpSFQ6MZ28RFj5cBQSLr92kQ+RoqvwJAWNdfDpVvIiORmKUnJGQSnxvIpLQmCHElu5PoUcydJ9FZohOdyMIsd4j24ILUiql+9xVJhawV3/leCV6+99F8oQJAXyIM1c7Hcswb7klHNt2pXmjWx0I2NZC+9orYw/3ID6KY44kfsfOjsNk/qoWJFQkrjisO/meCHHkx08IclGG5UQJBAKOFPL8a0RXRPSGhM6G94HP9nbG4tBYT7vfhvFSXst5H24pic0+Uy5s7GuyGS40iGhcP1pXMWlcSREUdnwVYY80=", SIGN_ALGORITHMS_256);
		System.out.println(sign);
	}

}
