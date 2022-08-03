package com.ay.common.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ay.common.constants.StatusCode;

/**
 * 校验参数的有效性
 * 
 * @author jackson
 *
 */
public class ValidateUtil implements StatusCode {

	/**
	 * 过滤空置
	 * 
	 * @param paramObj
	 *            json串对象
	 * @param params
	 *            所需验证的key字符串
	 * @return
	 */
	public static Map<String, Object> filterNull(JSONObject paramObj, String... params) {
		Map<String, Object> resultMap = new HashMap<>();
		if (params != null && params.length > 0) {
			for (String param : params) {
				String val = paramObj.getString(param);
				if (StringUtil.isNull(val)) {
					resultMap.put(STATUS, PARAM_IS_NULL);
					resultMap.put(MESSAGE, param + " is null!");
					return resultMap;
				}
			}
		}
		resultMap.put(STATUS, SUCCESS);
		resultMap.put(MESSAGE, "ok");
		return resultMap;
	}

	/**
	 * 过滤整型数字
	 * 
	 * @param paramObj
	 * @param params
	 * @return
	 */
	public static Map<String, Object> filterNumber(JSONObject paramObj, String... params) {
		Map<String, Object> resultMap = new HashMap<>();
		if (params != null && params.length > 0) {
			for (String param : params) {
				String val = paramObj.getString(param);
				if (!StringUtil.isNumeric(val)) {
					resultMap.put(STATUS, PARAM_NOT_NUMBER);
					resultMap.put(MESSAGE, param + " is not a number!");
					return resultMap;
				}
			}
		}
		resultMap.put(STATUS, SUCCESS);
		resultMap.put(MESSAGE, "ok");
		return resultMap;
	}
	
}
