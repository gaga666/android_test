package com.example.charge.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.charge.BuildConfig;

public class LogUtils {

    private static final boolean DEBUG = BuildConfig.DEBUG;
    // global tag
    private static final String LOG_TAG = "PROJECT_V_LOG";
    // ${global tag}: subtag: msg
    private static final String LOG_FORMAT = "%s: %s";

    private LogUtils() {}

    public static void error(String log) {
        if (DEBUG && !TextUtils.isEmpty(log))
            Log.e(LOG_TAG, log);
    }

    public static void log(String log) {
        if (DEBUG && !TextUtils.isEmpty(log))
            Log.i(LOG_TAG, log);
    }

    public static void log(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log))
            Log.i(LOG_TAG, String.format(LOG_FORMAT, tag, log));
    }

    public static void d(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log))
            Log.d(LOG_TAG, String.format(LOG_FORMAT, tag, log));
    }

    public static void e(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log))
            Log.e(LOG_TAG, String.format(LOG_FORMAT, tag, log));
    }

    public static void i(String tag, String log) {
        if (DEBUG && !TextUtils.isEmpty(tag) && !TextUtils.isEmpty(log))
            Log.i(LOG_TAG, String.format(LOG_FORMAT, tag, log));
    }
}
