package com.ay.pay.dubbo.service.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 支付参数vo
 * 
 * @author jackson
 *
 */
public class PayVo implements Serializable {

	private static final long serialVersionUID = 8273072197899047303L;

	private String siteId;
	private String username;
	private String clientId; // 商户
	private String type; // 支付类型
	private Byte payType; // 支付方式
	private BigDecimal money;
	private String bankCode; // 银行编码
	private String productName;

	private String bankNum; // 卡号
	private String trueName; // 姓名

	public PayVo() {
	}

	// 支付
	public PayVo(String siteId, String username, String clientId, String type, Byte payType, BigDecimal money, String bankCode, String productName) {
		this.siteId = siteId;
		this.username = username;
		this.clientId = clientId;
		this.type = type;
		this.payType = payType;
		this.money = money;
		this.bankCode = bankCode;
		this.productName = productName;
	}

	// 代付
	public PayVo(String siteId, String username, String clientId, String type, Byte payType, BigDecimal money, String bankNum, String productName, String trueName) {
		this.siteId = siteId;
		this.username = username;
		this.clientId = clientId;
		this.type = type;
		this.payType = payType;
		this.money = money;
		this.bankNum = bankNum;
		this.productName = productName;
		this.trueName = trueName;
	}

	public String getBankNum() {
		return bankNum;
	}

	public void setBankNum(String bankNum) {
		this.bankNum = bankNum;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
