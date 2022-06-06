package com.example.charge.api.model.dto;

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

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

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
