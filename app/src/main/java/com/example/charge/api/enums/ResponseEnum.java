package com.example.charge.api.enums;

public enum ResponseEnum {

    // Standard status code:
    // for detail: https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
    /** Client error responses.(part) */
    BAD_REQUEST(-400, "请求错误"),
    UNAUTHORIZED(-401, "未认证"),
    FORBIDDEN(-403, "访问权限不足"),
    NOT_FOUND(-404, "啥都木有"),
    METHOD_NOT_ALLOWED(-405, "This Request Method not support."),
    UNSUPPORTED_MEDIA_TYPE(-415, "Error: Unsupported Media Type."),

    /** Server error responses.(part) */
    INTERNAL_SERVER_ERROR(-500, "服务器错误"),
    SERVICE_UNAVAILABLE(-503, "服务暂不可用"),
    GATEWAY_TIMEOUT(-504, "服务调用超时"),

    /**
     * Successful response.
     *
     * 默认 msg 为'0', 视应用场景可能返回其他的 msg (如'登录成功'...).
     */
    SUCCESS(0, "0"),

    /** Custom error responses. */
    MAIL_USED(1001, "该邮箱已被使用"),
    USERNAME_USED(1002, "该用户名已被使用"),
    TEL_USED(1003, "该手机号已被使用"),

    UNREGISTERED_USER(1004, "该用户未注册"),
    WRONG_PASSWORD(1005, "密码错误"),

    INVALID_VERIFY_CODE(1006, "无效验证码"),
    EXPIRED_VERIFY_CODE(1007, "验证码已过期, 请重新获取"),

    INVALID_TOKEN(1008, "无效Token"),

    EXPIRED_ACCESS_TOKEN(1009, "Access Token已过期, 请重新获取"),

    EXPIRED_REFRESH_TOKEN(1010, "Refresh Token已过期, 请重新登录");

    private final int code;

    private final String msg;

    ResponseEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
