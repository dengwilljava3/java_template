package com.ay.common.service.base;

import java.util.HashMap;
import java.util.Map;

import com.ay.common.constants.StatusCode;

/**
 * 业务层基类,所有业务需继承此类
 * 
 * @author jackson
 *
 */
public class CommonService implements StatusCode {

	protected Map<String, Object> success(Object msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return this.success(resultMap, msg);
	}

	protected Map<String, Object> failure(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return this.failure(resultMap, msg);
	}

	protected Map<String, Object> maybe(String msg) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		return this.maybe(resultMap, msg);
	}

	protected Map<String, Object> success(Map<String, Object> resultMap, Object msg) {
		resultMap.put(STATUS, SUCCESS);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> failure(Map<String, Object> resultMap, String msg) {
		resultMap.put(STATUS, ERROR);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> maybe(Map<String, Object> resultMap, String msg) {
		resultMap.put(STATUS, MAYBE);
		resultMap.put(MESSAGE, msg);
		return resultMap;
	}

	protected Map<String, Object> result(String code, Object msg, Object data) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put(STATUS, code);
		resultMap.put(MESSAGE, msg);
		resultMap.put(DATA, data);
		return resultMap;
	}

	protected Map<String, Object> result(String code, Object msg) {
		return this.result(code, msg, null);
	}

}
