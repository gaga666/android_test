package com.example.charge.api.interceptor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.charge.MyApplication;
import com.example.charge.TokenManager;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.enums.ResponseEnum;
import com.example.charge.api.model.DataResponse;
import com.example.charge.api.model.MessageResponse;
import com.example.charge.api.model.dto.TokenPairInfo;
import com.example.charge.api.Api;
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
            // access_token ??????:
            if (res.getCode() == ResponseEnum.EXPIRED_ACCESS_TOKEN.getCode()) {
                synchronized (this) {
                    // ?????? refresh_token ???????????? token pair
                    final TokenPairInfo newTokenPairInfo = refreshTokenPair();
                    if (newTokenPairInfo == null) {
                        // refresh_token ???????????????(Reuse detection),
                        // TODO: ???????????????????????????????????? ??? ???????????????????????????
                        // ?????? token ??????
                        TokenManager.getInstance().clearInfo();
                        // TODO: ?????? ????????????
                        // ??????????????????????????????
                        LogUtils.log("EXPIRED_REFRESH_TOKEN || INVALID_TOKEN: ??????????????????????????????");
                        MyApplication.getContext()
                                .sendBroadcast(new Intent(Constants.INTENT_ACTION_FORCE_LOGOUT));
                    } else {
                        // ????????? access_token ????????????
                        Request newRequest = chain.request()
                                .newBuilder()
                                .header("Authorization", newTokenPairInfo.getTokenType() + " " + newTokenPairInfo.getAccessToken())
                                .build();
                        // ????????????
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
        // ???????????? Token pair
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
