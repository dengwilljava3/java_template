package com.ay.transfer.dubbo.service.vo;

import java.io.Serializable;

import com.ay.plantform.transfer.record.entity.ApiInfo;

/**
 * 登录参数
 * 
 * @author jackson
 *
 */
public class LoginParam implements Serializable {

	private static final long serialVersionUID = 7235750895547772513L;

	private ApiInfo apiInfo;

	private String username;

	private String oddType;

	private Byte terminalType; // 终端类型（1电脑，2手机）

	/**
	 * BBIN请详查附件二 (5042、5067、5068、5087、5903，尚未开放)
	 */
	private String gameType;// AG,BBIN需要

	/**
	 * 3：BB视讯<br>
	 * 5：BB电子<br>
	 * 15：3D电子<br>
	 * 19：AG视讯<br>
	 * 22：欧博视讯<br>
	 * 23：MG 电子<br>
	 * 24：OG视讯<br>
	 * 26：SA视讯<br>
	 * 27： GD视讯<br>
	 * 28：GNS 电子<br>
	 * 29：ISB电子<br>
	 * 30：BB捕鱼机
	 */
	private String gameKind;// BBIN需要

	/**
	 * 请详查附件三(gamekind=3时，需强制带入)
	 */
	private String gameCode;// BBIN需要

	public LoginParam(ApiInfo apiInfo, String username, String oddType, String gameType, String gameKind, String gameCode, Byte terminalType) {
		this.apiInfo = apiInfo;
		this.username = username;
		this.oddType = oddType;
		this.gameType = gameType;
		this.gameKind = gameKind;
		this.gameCode = gameCode;
		this.terminalType = terminalType;
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

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameKind() {
		return gameKind;
	}

	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public Byte getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(Byte terminalType) {
		this.terminalType = terminalType;
	}
}
