package com.ay.transfer.dubbo.service.vo;

import java.io.Serializable;

import com.ay.plantform.transfer.record.entity.ApiInfo;

/**
 * 创建用户参数
 * 
 * @author jackson
 *
 */
public class UserParam implements Serializable {

	private static final long serialVersionUID = 3082039883567017142L;

	private ApiInfo apiInfo;

	private String username;

	private String oddType;

	public UserParam(ApiInfo apiInfo, String username, String oddType) {
		this.apiInfo = apiInfo;
		this.username = username;
		this.oddType = oddType;
	}

	public ApiInfo getApiInfo() {
		return apiInfo;
	}

	public void setApiInfo(ApiInfo apiInfo) {
		this.apiInfo = apiInfo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOddType() {
		return oddType;
	}

	public void setOddType(String oddType) {
		this.oddType = oddType;
	}

}
