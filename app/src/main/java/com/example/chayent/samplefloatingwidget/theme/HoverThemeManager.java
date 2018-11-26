package com.example.chayent.samplefloatingwidget.theme;

import android.support.annotation.NonNull;

import de.greenrobot.event.EventBus;

/**
 * HoverThemeManager.java
 * SampleFloatingWidget
 * <p>
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class HoverThemeManager implements HoverThemer {
    private static HoverThemeManager sInstance;

    public static synchronized void init(@NonNull EventBus bus, @NonNull HoverTheme theme) {
        if (null == sInstance) {
            sInstance = new HoverThemeManager(bus, theme);
        }
    }

    public static synchronized HoverThemeManager getInstance() {
        if (null == sInstance) {
            throw new RuntimeException("Cannot obtain HoverThemeManager before calling init().");
        }

        return sInstance;
    }

    private EventBus mBus;
    private HoverTheme mTheme;

    public HoverThemeManager(@NonNull EventBus bus, @NonNull HoverTheme theme) {
        mBus = bus;
        setTheme(theme);
    }

    public HoverTheme getTheme() {
        return mTheme;
    }

    @Override
    public void setTheme(@NonNull HoverTheme theme) {
        mTheme = theme;
        mBus.postSticky(theme);
    }
}
