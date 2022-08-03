package com.ay.transfer.dubbo.service.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ay.plantform.transfer.record.entity.ApiInfo;

/**
 * 转账参数列表
 * 
 * @author jackson
 *
 */
public class TransferParam implements Serializable {

	private static final long serialVersionUID = -6451672840729366071L;

	private ApiInfo apiInfo;

	private String username;

	private BigDecimal money;

	private String mode; // IN OUT

	private Long transferRecordId;

	private boolean totalBalance; // 资金归集

	public TransferParam(ApiInfo apiInfo, String username, BigDecimal money, String mode, Long transferRecordId, boolean totalBalance) {
		this.apiInfo = apiInfo;
		this.username = username;
		this.money = money;
		this.mode = mode;
		this.transferRecordId = transferRecordId;
		this.totalBalance = totalBalance;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Long getTransferRecordId() {
		return transferRecordId;
	}

	public void setTransferRecordId(Long transferRecordId) {
		this.transferRecordId = transferRecordId;
	}

	public boolean isTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(boolean totalBalance) {
		this.totalBalance = totalBalance;
	}

}
