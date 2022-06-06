package com.example.charge;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;

import com.example.charge.api.model.dto.TokenPairInfo;
import com.example.charge.api.model.dto.UserInfo;
import com.example.charge.common.Constants;
import com.example.charge.utils.LogUtils;

public class UserInfoManager {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = UserInfoManager.class.getName();

    private static volatile UserInfoManager INSTANCE;

    private static Context sContext;

    private UserInfo mUserInfo;

    private UserInfoManager() {}

    public static UserInfoManager getInstance() {
        if (INSTANCE == null) {
            synchronized (TokenManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UserInfoManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init(Application context) {
        sContext = context;
        SharedPreferences sp = sContext.getSharedPreferences(Constants.SP_NAME_USER_INFO,
                                                             Context.MODE_PRIVATE);
        // 读取本地缓存数据
        Long uid = sp.getLong(Constants.KEY_UID, -1L);
        String uname = sp.getString(Constants.KEY_UNAME, "");
        String avatar = sp.getString(Constants.KEY_AVATAR, "");
        String sex = sp.getString(Constants.KEY_SEX, "");
        mUserInfo = new UserInfo()
                .setUid(uid)
                .setUsername(uname)
                .setAvatar(avatar)
                .setSex(sex);
        LogUtils.log(LOG_TAG, "Token manager: init.");
    }

    public Long getUid() {
        return mUserInfo.getUid();
    }

    public String getUsername() {
        return mUserInfo.getUsername();
    }

    public String getAvatar() {
        return mUserInfo.getAvatar();
    }

    public String getSex() {
        return mUserInfo.getSex();
    }

    public boolean hasUserInfo() {
        if (mUserInfo == null) {
            return false;
        }
        Long uid = mUserInfo.getUid();
        String uname = mUserInfo.getUsername();
        String avatar = mUserInfo.getAvatar();
        String sex = mUserInfo.getSex();
        return uid != null && uid > 0
                && uname != null && !"".equals(uname)
                && avatar != null && !"".equals(avatar)
                && sex != null && !"".equals(sex);
    }

    public void updateInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        // 更新本地缓存数据
        SharedPreferences sp = sContext.getSharedPreferences(Constants.SP_NAME_TOKEN_PAIR,
                                                             Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(Constants.KEY_UID, userInfo.getUid())
                .putString(Constants.KEY_UNAME, userInfo.getUsername())
                .putString(Constants.KEY_AVATAR, userInfo.getAvatar())
                .putString(Constants.KEY_SEX, userInfo.getSex())
                .apply();
        LogUtils.log(LOG_TAG, "User-Info manager: update info.");
    }

    public void clearInfo() {
        mUserInfo = null;
        SharedPreferences sp = sContext.getSharedPreferences(Constants.SP_NAME_USER_INFO,
                                                             Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
        LogUtils.log(LOG_TAG, "User-Info manager: clear info.");
    }

}
