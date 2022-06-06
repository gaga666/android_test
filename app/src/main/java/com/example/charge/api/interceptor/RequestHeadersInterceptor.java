package com.example.charge.api.interceptor;

import androidx.annotation.NonNull;

import com.example.charge.TokenManager;
import com.example.charge.utils.LogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestHeadersInterceptor implements Interceptor {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = RequestHeadersInterceptor.class.getName();

    private static final String HOST = "api.objectspace.top";

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        // 公共请求头
        Request.Builder builder = chain.request()
                .newBuilder()
                .addHeader("HOST", HOST)
                .addHeader("Connection", "Keep-Alive");
        // 如果请求头中不包含 'Authorization', 则添加 access_token
        // 避免使用 refresh_token 刷新 token 时, 重复添加相同 Header 导致值被覆盖
        if (!chain.request().headers().names().contains("Authorization")) {
            if (TokenManager.getInstance().hasTokenInfo()) {
                String authHeaderVal = TokenManager.getInstance().getTokenType()
                        + " " + TokenManager.getInstance().getAccessToken();
                builder.addHeader("Authorization", authHeaderVal);
                LogUtils.d(LOG_TAG, "add header: Authorization=" + authHeaderVal);
            }
        }


        return chain.proceed(builder.build());
    }
}
