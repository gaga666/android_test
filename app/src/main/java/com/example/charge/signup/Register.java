package com.example.charge.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.R;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.callback.ApiCallback;
import com.example.charge.api.Api;
import com.example.charge.E_mail;
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;

public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getName();
    EditText re_username, re_password, re_passwordSecond, re_mail, re_verify;
    LoadingDialog mLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button re_register = (Button) findViewById(R.id.re_register);
        re_username = (EditText) findViewById(R.id.re_username);
        re_password = (EditText) findViewById(R.id.re_password);
        re_passwordSecond = (EditText) findViewById(R.id.re_passwordSecond);
        re_mail = (EditText) findViewById(R.id.re_mail);
        re_verify = (EditText) findViewById(R.id.re_verify);
        re_register.setOnClickListener(view -> register());
        Button re_getVerify = findViewById(R.id.re_getVerify);
        re_getVerify.setOnClickListener(view -> sendEmail(re_mail.getText().toString()));
    }

    /**
     * 注册新用户
     */
    public void register() {
        String username = re_username.getText().toString();
        String password = re_password.getText().toString();
        String mail = re_mail.getText().toString();
        String code = re_verify.getText().toString();
        LogUtils.i(TAG, String.format("username -> %s, password -> %s, mail -> %s, code -> %s", username, password,mail,code));

        // show loading dialog
        showLoading();
        Api.register(username, password, mail, code, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    }
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                final String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "register().onFailure: " + log);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "register().onException: e -> " + e);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // destroy loading dialog
                        stopLoading();
                    }
                });

            }
        });
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("注册中...");
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
        Api.sendMail(to, 11, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this, "邮箱已发送", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                final String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "sendEmail().onFailure: " + log);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this, log, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "sendEmail().onException: e -> " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
