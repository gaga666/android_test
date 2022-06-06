package com.example.charge;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.charge.common.Constants;
import com.example.charge.api.model.dto.TokenPairInfo;
import com.example.charge.utils.LogUtils;

public class TokenManager {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = TokenManager.class.getName();

    private static volatile TokenManager INSTANCE;

    private static Context sContext;

    // 单独开辟一个线程来处理 Looper
    private HandlerThread mTokenThread;

    private Handler mTokenHandle;

    private TokenPairInfo mTokenPairInfo;

    public static TokenManager getInstance() {
        if (INSTANCE == null) {
            synchronized (TokenManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TokenManager();
//                    INSTANCE.mTokenThread = new HandlerThread("Token-HandlerThread");
//                    INSTANCE.mTokenHandle = new Handler(INSTANCE.mTokenThread.getLooper()) {
//                        @Override
//                        public void handleMessage(@NonNull Message msg) {
//                            super.handleMessage(msg);
//                            if (msg.obj != null) {
//                                switch (msg.what) {
//
//                                }
//                            } else {
//                                LogUtils.d(LOG_TAG, "getTokenHandler : msg is null");
//                            }
//                        }
//                    };
                }
            }
        }
        return INSTANCE;
    }

    public void init(Context context) {
        sContext = context;
        SharedPreferences sp = sContext.getApplicationContext()
                .getSharedPreferences(Constants.SP_NAME_TOKEN_PAIR, Context.MODE_PRIVATE);
        // 读取本地缓存数据
        String tokenType = sp.getString(Constants.KEY_TOKEN_TYPE, "");
        String accessToken = sp.getString(Constants.KEY_ACCESS_TOKEN, "");
        String refreshToken = sp.getString(Constants.KEY_REFRESH_TOKEN, "");
        mTokenPairInfo = new TokenPairInfo()
                .setTokenType(tokenType)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
        LogUtils.log(LOG_TAG, "Token manager: init.");
    }

    public String getTokenType() {
        return mTokenPairInfo.getTokenType();
    }

    public String getRefreshToken() {
        return mTokenPairInfo.getRefreshToken();
    }

    public String getAccessToken() {
        return mTokenPairInfo.getAccessToken();
    }

    public boolean hasTokenInfo() {
        if (mTokenPairInfo == null) {
            return false;
        }
        String tokenType = mTokenPairInfo.getTokenType();
        String accessToken = mTokenPairInfo.getAccessToken();
        String refreshToken = mTokenPairInfo.getRefreshToken();
        return tokenType != null && !"".equals(tokenType)
                && accessToken != null && !"".equals(accessToken)
                && refreshToken != null && !"".equals(refreshToken);
    }

    public void updateInfo(TokenPairInfo tokenPairInfo) {
        this.mTokenPairInfo = tokenPairInfo;
        // 更新本地缓存数据
        SharedPreferences sp = sContext.getApplicationContext()
                .getSharedPreferences(Constants.SP_NAME_TOKEN_PAIR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.KEY_TOKEN_TYPE, tokenPairInfo.getTokenType())
                .putString(Constants.KEY_REFRESH_TOKEN, tokenPairInfo.getRefreshToken())
                .putString(Constants.KEY_ACCESS_TOKEN, tokenPairInfo.getAccessToken())
                .apply();
        LogUtils.log(LOG_TAG, "Token manager: update info.");
    }

    public void clearInfo() {
        mTokenPairInfo = null;
        SharedPreferences sp = sContext.getApplicationContext()
                .getSharedPreferences(Constants.SP_NAME_TOKEN_PAIR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        LogUtils.log(LOG_TAG, "Token manager: clear info.");
    }
}
