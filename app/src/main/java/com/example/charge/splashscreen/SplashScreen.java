package com.example.charge.splashscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.BaseActivity;
import com.example.charge.LoopView;
import com.example.charge.MyApplication;
import com.example.charge.R;
import com.example.charge.TokenManager;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.callback.ApiDataCallback;
import com.example.charge.api.enums.ResponseEnum;
import com.example.charge.api.model.dto.UserInfo;
import com.example.charge.api.Api;
import com.example.charge.common.Constants;
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends BaseActivity {

    private static final String TAG = SplashScreen.class.getName();

    // 1.5 seconds
    private static final long DELAY = 1500L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        init();
    }

    private void init() {
        long startTime = System.currentTimeMillis();

        if (TokenManager.getInstance().hasTokenInfo()) {
            // 有 token 信息, 尝试当前登录用户的信息
            // 如果正常返回, 则说明 token 有效, 进入主界面
            Api.getMyInfo(new ApiDataCallback<UserInfo>() {
                @Override
                public void onSuccess(@NonNull UserInfo data) {
                    // TODO: 保存用户信息
                    Intent intent = new Intent(SplashScreen.this, LoopView.class);
                    // 不足2秒则延迟, 避免一闪而过
                    if (System.currentTimeMillis() - startTime < DELAY) {
                        Timer timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        };
                        timer.schedule(task, DELAY - (System.currentTimeMillis() - startTime));
                    } else {
                        startActivity(intent);
                        finish();
                    }
                }
                @Override
                public void onFailure(int errCode, @NonNull String errMsg) {
                    if (errCode == ResponseEnum.INVALID_TOKEN.getCode()) {
                        // access_token 提前失效(Reuse detection):
                        // 发送全局广播强制登出
                        LogUtils.log("发送全局广播强制登出");
                        MyApplication.getContext()
                                .sendBroadcast(new Intent(Constants.INTENT_ACTION_FORCE_LOGOUT));
                        // 清理失效的 token 信息
                        TokenManager.getInstance().clearInfo();
                    }
                }
                @Override
                public void onException(@NonNull ApiException e) {
                    LogUtils.e(TAG, "onException: e -> " + e);
                }
            });
        } else {
            // 没有 token 信息, 进入登录界面
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            // 不足2秒则延迟, 避免一闪而过
            if (System.currentTimeMillis() - startTime < DELAY) {
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                };
                timer.schedule(task, DELAY - (System.currentTimeMillis() - startTime));
            } else {
                startActivity(intent);
                finish();
            }
        }



    }
}