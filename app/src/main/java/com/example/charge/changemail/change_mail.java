package com.example.charge.changemail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.charge.E_mail;
import com.example.charge.R;
import com.example.charge.api.exception.ApiException;
import com.example.charge.api.callback.ApiCallback;
import com.example.charge.api.Api;
import com.example.charge.login.LoginActivity;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;

public class change_mail extends AppCompatActivity {

    private static final String TAG = change_mail.class.getName();

    EditText mail_old_mail, mail_new_mail,mail_verify;
    Button mail_getVerify, mail_reset;
    LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mail);
        initView();
    }

    private void initView(){
        mail_new_mail = findViewById(R.id.mail_new_mail);
        mail_old_mail = findViewById(R.id.mail_old_mail);
        mail_verify = findViewById(R.id.mail_verify);
        mail_getVerify = findViewById(R.id.mail_getVerify);
        mail_reset = findViewById(R.id.mail_reset);

        // set click listener
        mail_getVerify.setOnClickListener(view -> {
            sendEmail(mail_old_mail.getText().toString());
        });
        mail_reset.setOnClickListener(view -> {
            String oldMail = mail_old_mail.getText().toString();
            String newMail = mail_new_mail.getText().toString();
            String code = mail_verify.getText().toString();
            updateMail(oldMail, newMail, code);
        });
    }

    public void updateMail(String oldMail, String newMail, String code) {
        LogUtils.i(TAG, String.format("oldMail -> %s, code -> %s, newMail -> %s", oldMail, code, newMail));
        // show loading dialog
        showLoading();
        Api.changeMail(oldMail, newMail, code, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();

                    Toast.makeText(change_mail.this, "修改成功", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(change_mail.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "sendEmail().onFailure: " + log);

                runOnUiThread(() -> {
                    Toast.makeText(change_mail.this, log, Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "updateMail().onException: e -> " + e);

                runOnUiThread(() -> {
                    // destroy loading dialog
                    stopLoading();
                });
            }
        });
    }

    public void sendEmail(String to) {
        if(!E_mail.isValidEmail(to)) {
            Toast.makeText(this, "请输入有效邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        Api.sendMail(to,14, new ApiCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    Toast.makeText(change_mail.this, "邮箱已发送", Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                String log = "errCode: " + errCode + ", errMsg: " + errMsg;
                LogUtils.e(TAG, "sendEmail().onFailure: " + log);

                runOnUiThread(() -> {
                    Toast.makeText(change_mail.this, log, Toast.LENGTH_SHORT).show();
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "sendEmail().onException: e -> " + e);
            }
        });
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("修改中...");
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.show();
        }
    }

    private void stopLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}