package com.example.chayent.samplefloatingwidget;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.example.chayent.samplefloatingwidget.AppState.AppStateTracker;
import com.example.chayent.samplefloatingwidget.theme.HoverTheme;
import com.example.chayent.samplefloatingwidget.theme.HoverThemeManager;

/**
 * App.java
 * SampleFloatingWidget
 * <p>
 * Created by Chayen Tansritrang on 11/27/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupTheme();
        setupAppStateTracking();
    }

    private void setupTheme() {
        HoverTheme defaultTheme = new HoverTheme(
                ContextCompat.getColor(this, R.color.hover_accent),
                ContextCompat.getColor(this, R.color.hover_base));
        HoverThemeManager.init(Bus.getInstance(), defaultTheme);
    }

    private void setupAppStateTracking() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        AppStateTracker.init(this, Bus.getInstance());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activityManager.getAppTasks().size() > 0) {
                AppStateTracker.getInstance().trackTask(activityManager.getAppTasks().get(0).getTaskInfo());
            }
        }
    }
}
