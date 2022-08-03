package com.ay.rbac.vo;

import java.util.Date;

import com.ay.rbac.entity.Client;
import com.ay.rbac.entity.User;

public class ClientVo extends Client {

	private static final long serialVersionUID = 8000586326438540776L;

	private User user;

	private String sessionId;

	public ClientVo() {
	}

	public ClientVo(Long id, String name, String clientId, String clientKey, String remark, Date createTime, Date updateTime, User user) {
		super(id, name, clientId, clientKey, remark, createTime, updateTime);
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
