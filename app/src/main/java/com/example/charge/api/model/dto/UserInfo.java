package com.example.charge.api.model.dto;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 用户名
     */
    @JsonProperty("uname")
    private String username;

    /**
     * 用户头像图片链接
     */
    private String avatar;

    /**
     * 用户性别
     */
    private String sex;

    public Long getUid() {
        return uid;
    }

    public UserInfo setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserInfo setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public UserInfo setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public UserInfo setSex(String sex) {
        this.sex = sex;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", face='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
