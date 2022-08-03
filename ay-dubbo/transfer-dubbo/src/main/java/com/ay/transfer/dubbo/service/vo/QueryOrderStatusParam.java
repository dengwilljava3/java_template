package com.ay.transfer.dubbo.service.vo;

import java.io.Serializable;

import com.ay.plantform.transfer.record.entity.ApiInfo;

/**
 * 查询订单状态参数
 * 
 * @author jackson
 *
 */
public class QueryOrderStatusParam implements Serializable {

	private static final long serialVersionUID = 2387454019916782L;

	private ApiInfo apiInfo;

	private String orderNum;

	public QueryOrderStatusParam(ApiInfo apiInfo, String orderNum) {
		this.apiInfo = apiInfo;
		this.orderNum = orderNum;
	}

	public ApiInfo getApiInfo() {
		return apiInfo;
	}

	public void setApiInfo(ApiInfo apiInfo) {
		this.apiInfo = apiInfo;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

}
