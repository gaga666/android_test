package com.example.charge.api.callback;

import androidx.annotation.NonNull;

import com.example.charge.api.exception.ApiException;
import com.example.charge.api.enums.ResponseEnum;

public interface ApiCallback {

    /**
     * 当 API 响应 code == ({@link ResponseEnum#SUCCESS}) 时回调
     *
     */
    void onSuccess();

    /**
     * 当 API 响应 code != 0 时回调
     *
     * @param errCode 错误码
     * @param errMsg 错误信息
     */
    void onFailure(int errCode, @NonNull String errMsg);

    /**
     * 当 API 请求异常时回调(服务端无法处理请求, 客户端网络异常...)
     * @param e 异常
     */
    void onException(@NonNull ApiException e);
}
