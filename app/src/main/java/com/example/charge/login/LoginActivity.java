package com.example.charge.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.charge.LoopView;
import com.example.charge.R;
import com.example.charge.TokenManager;
import com.example.charge.api.callback.ApiDataCallback;
import com.example.charge.api.ApiException;
import com.example.charge.api.remote.Api;
import com.example.charge.entity.TokenPairInfo;
import com.example.charge.entity.UserInfo;
import com.example.charge.resetpassword;
import com.example.charge.signup.Register;
import com.example.charge.utils.LogUtils;
import com.example.charge.view.LoadingDialog;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getName();

    // loading dialog
    private LoadingDialog mLoadingDialog;
    private ImageView lg_userIcon;
    private EditText lg_username, lg_password;
    private TextView lg_forgetPsd;
    private CheckBox lg_rememberPsd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {

        lg_username =  findViewById(R.id.lg_username);
        lg_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String username = ((EditText) v).getText().toString();
                    if (!"".equals(username)) {
                        showAvatarOf(username);
                    }
                }
            }
        });

        lg_password =  findViewById(R.id.lg_password);
        lg_userIcon = findViewById(R.id.lg_userIcon);
//        Glide.with(this).load("http://s0.objectspace.top/fs/avatar/no-avatar.jpg").into(lg_userIcon);

        Button lg_login =  findViewById(R.id.lg_login);
        lg_login.setOnClickListener(view -> login());
        lg_forgetPsd = findViewById(R.id.lg_forgetPsd);
        lg_forgetPsd.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, resetpassword.class);
            startActivity(i);
            overridePendingTransition(0,0);
        });


        /**
         * 跳转到注册页面
         */
        TextView lg_textView =  findViewById(R.id.lg_textview);
        lg_textView.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, Register.class);
            startActivity(i);
            overridePendingTransition(0,0);
        });

        /**
         *
         * 记住密码
         */
        lg_rememberPsd =  findViewById(R.id.lg_rememberPsd);
        sharedPreferences = getSharedPreferences("items",MODE_PRIVATE);
        boolean isRemember = sharedPreferences.getBoolean("flag",false);
        if(isRemember){
            getData();
            lg_rememberPsd.performClick();
        }
        lg_rememberPsd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
                if (isCheck){
                    saveData();
                }else {
                    clearData();
                }
            }
        });
    }

    private void clearData() {
        sharedPreferences = getSharedPreferences("items",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        lg_username.setText("");
        lg_password.setText("");
    }

    private void getData(){
        sharedPreferences = getSharedPreferences("items", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("lg_username","");
        String password = sharedPreferences.getString("lg_password","");
        lg_username.setText(username);
        TransformationMethod method = PasswordTransformationMethod.getInstance();
        lg_password.setText(password);
        lg_password.setTransformationMethod(method);
    }

    private void saveData() {
        sharedPreferences = getSharedPreferences("items", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String  username = lg_username.getText().toString();
        String password = lg_password.getText().toString();
        if (!username.equals("") && !password.equals("")){
            editor.putString("lg_username",username);
            editor.putString("lg_password",password);
            editor.putBoolean("flag",true);
            editor.commit();
        }
    }

    /**
     * 根据输入的用户名显示用户头像
     * @param username 用户名
     */
    private void showAvatarOf(String username) {
        Api.getUserInfoByUsername(username, new ApiDataCallback<UserInfo>() {
            @Override
            public void onSuccess(@NonNull UserInfo data) {
                String avatar = data.getAvatar();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this)
                                .load(avatar)
                                .into(lg_userIcon);
                    }
                });
            }
            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                LogUtils.e(TAG, "onFailure(): code == " + errCode + ", msg: " + errMsg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this)
                                .load("http://s0.objectspace.top/fs/avatar/no-avatar.jpg")
                                .into(lg_userIcon);
                    }
                });
            }
            @Override
            public void onException(@NonNull ApiException e) {
                LogUtils.e(TAG, "onError(): e -> " + e);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this)
                                .load("http://s0.objectspace.top/fs/avatar/no-avatar.jpg")
                                .into(lg_userIcon);
                    }
                });
            }
        });
    }

    /**
     * 判断登录信息
     */
    private void login() {
        String username = lg_username.getText().toString();
        String password = lg_password.getText().toString();
        LogUtils.i(TAG, String.format("username -> %s, password -> %s", username, password));
        // show loading dialog
        showLoading();
        Api.login(username, password, new ApiDataCallback<TokenPairInfo>() {
            @Override
            public void onSuccess(@NonNull TokenPairInfo data) {
                // destroy loading dialog
                stopLoading();
                // 保存 Token
                TokenManager.getInstance().updateInfo(data);
                // 登录成功, 跳转到主界面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, LoopView.class);
                        startActivity(i);
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(int errCode, @NonNull String errMsg) {
                // destroy loading dialog
                stopLoading();
                LogUtils.d(TAG, "onFailure(): code: " + errCode);
                LogUtils.d(TAG, "onFailure(): msg: " + errMsg);
            }

            @Override
            public void onException(@NonNull ApiException e) {
                // destroy loading dialog
                stopLoading();
                LogUtils.e(TAG, "onError: e -> " + e );
            }
        });
    }

    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setText("登录中...");
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