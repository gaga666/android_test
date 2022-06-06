package com.example.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.example.charge.login.LoginActivity;

public class ForceLogoutReceiver extends BroadcastReceiver {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = ForceLogoutReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("退出登录通知")
                .setMessage("您的登录信息已过期, 请重新登录")
                // 设置不可通过点击返回键或空白区域关闭对话框
                .setCancelable(false)
                .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 销毁所有 Activity
                        ActivityCollector.finishAll();
                        // 重新启动登录界面
                        Intent i = new Intent(context, LoginActivity.class);
                        context.startActivity(i);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
