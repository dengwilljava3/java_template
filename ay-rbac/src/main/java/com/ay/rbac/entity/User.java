package com.ay.rbac.entity;

import java.util.Date;

public class User {
    private Long id;

    private String username;

    private String password;

    private String payPwd;

    private String saltPay;

    private String name;

    private String tel;

    private String email;

    private Byte enable;

    private Byte autoGrab;

    private Date createTime;

    private Date updateTime;

    public User(Long id, String username, String password, String payPwd, String saltPay, String name, String tel, String email, Byte enable, Byte autoGrab, Date createTime, Date updateTime) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.payPwd = payPwd;
        this.saltPay = saltPay;
        this.name = name;
        this.tel = tel;
        this.email = email;
        this.enable = enable;
        this.autoGrab = autoGrab;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public User() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd == null ? null : payPwd.trim();
    }

    public String getSaltPay() {
        return saltPay;
    }

    public void setSaltPay(String saltPay) {
        this.saltPay = saltPay == null ? null : saltPay.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Byte getEnable() {
        return enable;
    }

    public void setEnable(Byte enable) {
        this.enable = enable;
    }

    public Byte getAutoGrab() {
        return autoGrab;
    }

    public void setAutoGrab(Byte autoGrab) {
        this.autoGrab = autoGrab;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}