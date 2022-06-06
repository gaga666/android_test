package com.example.charge;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
    /**
     * Auto-generated: the log tag
     */
    private static final String LOG_TAG = ActivityCollector.class.getName();

    private static List<Activity> sActivityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    public static void finishAll() {
        // First: finish all alive activities
        for (Activity activity : sActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
        // Clear all activities
        sActivityList.clear();
    }
}
