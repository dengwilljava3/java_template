package com.ay.rbac.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.ay.common.util.DateUtil;
import com.ay.common.util.EncUtil;
import com.ay.common.util.JSONUtil;
import com.ay.common.util.RandomUtil;
import com.ay.common.util.httpclient.HttpClientUtil;

public class UserControllerTest {
	public static String url_prod = "http://47.245.57.235:8091/";
	public static String url_local = "http://localhost:8091/";
	String clientId = "yq";
	String timestamp = DateUtil.format(DateUtil.YYMMDD, new Date());
	String key = "qwaszxyq001";
	String username = "admin";
	String password = "admin";

	@Test
	public void loginNoValid() {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("username", username);
		paramMap.put("password", password);
		System.out.println(EncUtil.toMD5(password));
		String result = HttpClientUtil.sendPostByJson(url_prod + "loginNoValid", JSONUtil.map2Json(paramMap));
		System.out.println(result);
		JSONObject parseObject = JSONObject.parseObject(result);
		// 1. 查询部门用户业务
//		paramMap.clear();
		JSONObject messageObj = parseObject.getJSONObject("message");
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("clientId", messageObj.getString("clientId"));
		headerMap.put("timestamp", timestamp);
		headerMap.put("sessionId", "155392345455843cb7ae49d4443fbb7f5541bf7a7aea1");
		headerMap.put("encrypt", RandomUtil.generateMixString(5) + EncUtil.hexSha1(clientId + timestamp + key) + RandomUtil.generateMixString(6));
		
		paramMap.put("clientId", messageObj.getString("clientId"));
		//{"clientId":"yq","user":{"username":"test","name":"df","departmentIds":[3],"roleIds":[1]}}
		Map<String, Object> userMap = new HashMap<>();
		userMap.put("username", "test");
		userMap.put("name", "df");
		List<Long> departmentIds = new ArrayList<>();
		departmentIds.add(3l);
		userMap.put("departmentIds", departmentIds);
		List<Long> roleIds = new ArrayList<>();
		departmentIds.add(1l);
		userMap.put("roleIds", roleIds);
		saveUser(paramMap, headerMap);
//		queryDepartmentUsers(paramMap, messageObj, headerMap);
//	getLoginNoValidTest(paramMap, messageObj, headerMap);
//	getMenuByUsername(paramMap, messageObj, headerMap);
	}

	private void saveUser(Map<String, Object> paramMap, Map<String, String> headerMap) {
		String result = HttpClientUtil.sendPostByJson(url_prod + "saveUser", JSONUtil.map2Json(paramMap), headerMap);
		System.out.println(result);
	}

	private void getMenuByUsername(Map<String, Object> paramMap, JSONObject messageObj, Map<String, String> headerMap) {
		String result;
		paramMap.put("username", messageObj.getJSONObject("user").getString("username"));
		result = HttpClientUtil.sendPostByJson(url_local + "getMenuByUsername", JSONUtil.map2Json(paramMap), headerMap);
		System.out.println(result);
	}

	private void getLoginNoValidTest(Map<String, Object> paramMap, JSONObject messageObj, Map<String, String> headerMap) {
		String result;
		paramMap.put("username", messageObj.getJSONObject("user").getString("username"));
		result = HttpClientUtil.sendPostByJson(url_prod + "getLoginUserNoValid", JSONUtil.map2Json(paramMap), headerMap);
		System.out.println(result);
	}

	/**
	 * 查询部门用户
	 * 
	 * @param paramMap
	 * @param messageObj
	 * @param headerMap
	 */
	private void queryDepartmentUsers(Map<String, Object> paramMap, JSONObject messageObj, Map<String, String> headerMap) {
		String result;
		paramMap.put("siteId", messageObj.getString("clientId"));
		paramMap.put("username", username);
		paramMap.put("pageNum", 1);
		paramMap.put("pageSize", 20);
		result = HttpClientUtil.sendPostByJson(url_local + "queryDepartmentUsers", JSONUtil.map2Json(paramMap), headerMap);
		System.out.println("queryDeartmentUsers result  = " + result);
	}
}
