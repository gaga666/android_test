package com.example.charge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.api.ApiException;
import com.example.charge.api.callback.ApiCallback;
import com.example.charge.api.remote.Api;
import com.example.charge.api.model.MessageResponse;
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class resetpassword extends AppCompatActivity {
    private static final String TAG = resetpassword.class.getName();
    EditText reset_username;
    EditText reset_password;
    EditText reset_mail;
    EditText reset_verify;
    LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        reset_password =  findViewById(R.id.reset_password);
        reset_username =  findViewById(R.id.reset_username);
        reset_verify =  findViewById(R.id.reset_verify);
        reset_mail =  findViewById(R.id.reset_mail);
        Button reset_getVerify = findViewById(R.id.reset_getVerify);
        reset_getVerify.setOnClickListener(view -> sendEmail(reset_mail.getText().toString()));
        Button reset_reset = findViewById(R.id.reset_reset);
        reset_reset.setOnClickListener(view -> updatePwd(reset_username.getText().toString(), reset_password.getText().toString(), reset_verify.getText().toString()));
    }

    public void updatePwd(String username, String newPwd, String code) {
        String password = reset_password.getText().toString();
        String mail = reset_mail.getText().toString();
        LogUtils.i(TAG, String.format("username -> %s, password -> %s, mail -> %s, code -> %s, newPwd -> %s", username, password,mail,code,newPwd));

        // show loading dialog
        showLoading();
        Api.changePwd(newPwd, mail, code, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();

                    Toast.makeText(resetpassword.this, "修改成功", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(resetpassword.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "updatePwd().onFailure: " + log);
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();

                    Toast.makeText(resetpassword.this, log, Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "updatePwd().onException: e -> " + e);

                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
            }
        });
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("修改中...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    public void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void sendEmail(String to) {
        if(!E_mail.isValidEmail(to)) {
            Toast.makeText(this, "请输入有效邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        Api.sendMail(to,13, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(resetpassword.this, "发送成功", Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "sendEmail().onFailure: " + log);

                runOnUiThread(() -> {
                    Toast.makeText(resetpassword.this, log, Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "sendEmail().onException: e -> " + e);

                runOnUiThread(() -> {
                    Toast.makeText(resetpassword.this, "发送失败", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
