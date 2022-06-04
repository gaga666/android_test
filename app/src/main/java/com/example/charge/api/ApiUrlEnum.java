package com.example.charge.api;

import androidx.annotation.NonNull;

public enum ApiUrlEnum {
    
    /** 注册 */
    SIGNUP("passport/signup"),

    /** 登录 */
    LOGIN("passport/login"),

    /** 发送邮件验证码 */
    SEND_MAIL("passport/mail/send"),

    /** 上传图片 */
    UPLOAD_ALBUM("generic/fs/upload_album"),

    /** 获取用户信息 */
    GET_USER_INFO("account/info"),

    /** 获取自己信息 */
    GET_MY_INFO("account/myinfo"),

    /** 更换密码 */
    CHANGE_PWD("account/change/pwd"),

    /** 换绑邮箱 */
    CHANGE_MAIL("account/change/mail"),

    /** 换头像 */
    CHANGE_AVATAR("account/change/avatar");

    private final String url;

    public String getUrl() {
        return url;
    }

    ApiUrlEnum(String var) {
        this.url = var;
    }

    @NonNull
    @Override
    public String toString() {
        return this.url;
    }
}
