package com.example.chayent.samplefloatingwidget.theme;

import android.support.annotation.ColorInt;

/**
 * HoverTheme.java
 * SampleFloatingWidget
 * <p>
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class HoverTheme {

    @ColorInt
    private int mAccentColor;

    @ColorInt
    private int mBaseColor;

    public HoverTheme(@ColorInt int accentColor, @ColorInt int baseColor) {
        mAccentColor = accentColor;
        mBaseColor = baseColor;
    }

    @ColorInt
    public int getAccentColor() {
        return mAccentColor;
    }

    @ColorInt
    public int getBaseColor() {
        return mBaseColor;
    }
}
