package com.example.charge.splashscreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.LoopView;
import com.example.charge.MyApplication;
import com.example.charge.R;
import com.example.charge.login.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    // 2 seconds
    private static final long DELAY = 2 * 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        init();
    }

    private void init() {
        long startTime = System.currentTimeMillis();

        String accessToken = MyApplication.getAccessToken();
        String tokenType = MyApplication.getTokenType();
        Intent intent;
        if (accessToken == null || "".equals(accessToken)) {
            // 没有 token 信息, 进入登录界面
            intent = new Intent(SplashScreen.this, LoginActivity.class);

        } else {
            // 有 token 信息, 进入主界面
            intent = new Intent(SplashScreen.this, LoopView.class);
        }

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