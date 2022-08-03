package com.ay.common.util;

import java.util.UUID;

/**
 * uuid生成器
 * 
 * @author jackson
 *
 */
public class UUIDUtil {

	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
