package com.ay.common.controller.base;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.ay.common.constants.StatusCode;
import com.ay.common.util.StringUtil;

/**
 * 控制层基类,所有控制层必须继承此基类
 * 
 * @author jackson
 *
 */
public class BaseController2 implements StatusCode {
	
	final String DATA = "result";
	protected final String STATUS = "code";
	final String MESSAGE = "msg";

	/**
	 * 获取真实ip
	 */
	protected String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");
		if (!StringUtil.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (!StringUtil.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
//		String ip = request.getHeader("x-forwarded-for");
//		if (ip == null) {
//			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//				ip = request.getHeader("Proxy-Client-IP");
//			}
//			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//				ip = request.getHeader("WL-Proxy-Client-IP");
//			}
//			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//				ip = request.getHeader("HTTP_CLIENT_IP");
//			}
//			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//			}
//			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//				ip = request.getRemoteAddr();
//			}
//		}
//		return ip;
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

	protected String getSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if ("sessionId".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

	protected String getSessionIdInHeader(HttpServletRequest request) {
		return request.getHeader("sessionId");
	}
}
