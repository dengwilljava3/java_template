package com.ay.transfer.dubbo.service.vo;

import java.io.Serializable;

import com.ay.plantform.transfer.record.entity.ApiInfo;

public class QueryBalanceParam implements Serializable {

	private static final long serialVersionUID = 1165840699037956421L;

	private ApiInfo apiInfo;

	private String username;

	public QueryBalanceParam(ApiInfo apiInfo, String username) {
		this.apiInfo = apiInfo;
		this.username = username;
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

}
