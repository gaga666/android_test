package com.example.charge.api;

import androidx.annotation.NonNull;

public enum ApiUrlEnum {
    /** 注册 */
    SIGNUP("passport/signup"),

    /** 登录 */
    LOGIN("passport/login"),

    /** 发送邮件验证码 */
    SEND_MAIL("passport/mail/send"),

    /** 更换密码 */
    CHANGE_PWD("passport/change/pwd"),

    /** 换绑邮箱 */
    CHANGE_MAIL("passport/change/mail"),

    /** 上传图片 */
    UPLOAD_ALBUM("generic/fs/upload_album");

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
