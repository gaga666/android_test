package com.example.charge;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.common.Constants;

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = ActivityCollector.class.getName();

    private ForceLogoutReceiver receiver;

    @Override
    protected void onResume() {
        super.onResume();
        // 注册强制登出广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.INTENT_ACTION_FORCE_LOGOUT);
        receiver = new ForceLogoutReceiver();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
