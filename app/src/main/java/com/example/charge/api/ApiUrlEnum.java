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

    /** 更换密码 */
    CHANGE_PWD("space/change/pwd"),

    /** 换绑邮箱 */
    CHANGE_MAIL("space/change/mail"),

    /** 获取用户信息 */
    GET_USER_INFO("space/acc/info");

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
