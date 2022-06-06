package com.example.charge.api.interceptor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.charge.MyApplication;
import com.example.charge.TokenManager;
import com.example.charge.api.ApiException;
import com.example.charge.api.enums.ResponseEnum;
import com.example.charge.api.model.DataResponse;
import com.example.charge.api.model.MessageResponse;
import com.example.charge.api.model.dto.TokenPairInfo;
import com.example.charge.api.remote.Api;
import com.example.charge.common.Constants;
import com.example.charge.utils.LogUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TokenInterceptor implements Interceptor {

    private static final String TAG = TokenInterceptor.class.getName();

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        LogUtils.d(TAG, "Authorization=" + request.header("Authorization"));

        Response response = chain.proceed(request);

        if (!request.headers().names().contains("Authorization")) {
            return response;
        }

        LogUtils.d(TAG, "response.code=" + response.code());

        String jsonStr = "";
        MediaType mediaType = null;

        ResponseBody resBody = response.body();
        if (resBody != null) {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            jsonStr = resBody.string();
            LogUtils.d(TAG, "jsonStr: " + jsonStr);
            mediaType = resBody.contentType();
            LogUtils.d(TAG, "mediaType: " + mediaType);
            MessageResponse res = mapper.readValue(jsonStr, MessageResponse.class);
            LogUtils.d(TAG, "res.getCode()=" + res.getCode());
            // access_token 过期:
            if (res.getCode() == ResponseEnum.EXPIRED_ACCESS_TOKEN.getCode()) {
                synchronized (this) {
                    // 使用 refresh_token 更新获取 token pair
                    final TokenPairInfo newTokenPairInfo = refreshTokenPair();
                    if (newTokenPairInfo == null) {
                        // refresh_token 过期或失效(Reuse detection),
                        // TODO: 弹出提示要求用户重新登录 并 清理保存的用户信息
                        // 清除 token 信息
                        TokenManager.getInstance().clearInfo();
                        // TODO: 清除 用户信息
                        // 发送全局广播强制登出
                        LogUtils.log("EXPIRED_REFRESH_TOKEN || INVALID_TOKEN: 发送全局广播强制登出");
                        MyApplication.getContext()
                                .sendBroadcast(new Intent(Constants.INTENT_ACTION_FORCE_LOGOUT));
                    } else {
                        // 使用新 access_token 重新请求
                        Request newRequest = chain.request()
                                .newBuilder()
                                .header("Authorization", newTokenPairInfo.getTokenType() + " " + newTokenPairInfo.getAccessToken())
                                .build();
                        // 重新请求
                        return chain.proceed(newRequest);
                    }
                }
            }
        } else {
            LogUtils.e(TAG, "response.body() == null");
        }

        return response.newBuilder()
                .body(ResponseBody
                .create(jsonStr, mediaType))
                .build();
    }

    private TokenPairInfo refreshTokenPair() throws IOException {
        // 尝试刷新 Token pair
        try {
            DataResponse<TokenPairInfo> res = Api.refreshTokenPair();
            LogUtils.d(TAG, "refresh, code: " + res.getCode());
            LogUtils.d(TAG, "refresh, msg: " + res.getMessage());
            LogUtils.d(TAG, "refresh, data: " + res.getData());

            if (res.getCode() == ResponseEnum.EXPIRED_REFRESH_TOKEN.getCode()
                    || res.getCode() == ResponseEnum.INVALID_TOKEN.getCode()) {
                return null;
            } else {
                TokenPairInfo tokenPairInfo = res.getData();
                SharedPreferences sp = MyApplication.getContext()
                        .getSharedPreferences(Constants.SP_NAME_TOKEN_PAIR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.KEY_REFRESH_TOKEN, tokenPairInfo.getRefreshToken())
                        .putString(Constants.KEY_ACCESS_TOKEN, tokenPairInfo.getAccessToken())
                        .putString(Constants.KEY_TOKEN_TYPE, tokenPairInfo.getTokenType());
                editor.commit();
            }
            return res.getData();
        } catch (ApiException e) {
            LogUtils.e(TAG, e.toString());
            return null;
        }
    }
}
