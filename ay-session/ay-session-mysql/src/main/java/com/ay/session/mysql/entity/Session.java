package com.ay.session.mysql.entity;

import java.util.Date;

public class Session {
    private Long id;

    private String sessionId;

    private String username;

    private Date lastRequestTime;

    private Byte timeout;

    private Byte privileged;

    public Session(Long id, String sessionId, String username, Date lastRequestTime, Byte timeout, Byte privileged) {
        this.id = id;
        this.sessionId = sessionId;
        this.username = username;
        this.lastRequestTime = lastRequestTime;
        this.timeout = timeout;
        this.privileged = privileged;
    }

    public Session() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Date getLastRequestTime() {
        return lastRequestTime;
    }

    public void setLastRequestTime(Date lastRequestTime) {
        this.lastRequestTime = lastRequestTime;
    }

    public Byte getTimeout() {
        return timeout;
    }

    public void setTimeout(Byte timeout) {
        this.timeout = timeout;
    }

    public Byte getPrivileged() {
        return privileged;
    }

    public void setPrivileged(Byte privileged) {
        this.privileged = privileged;
    }
}