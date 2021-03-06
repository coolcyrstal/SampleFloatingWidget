package com.example.chayent.samplefloatingwidget.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * ChatTabView.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatTabView extends View {

    private int mBackgroundColor;
    private Integer mForegroundColor;

    private Drawable mCircleDrawable;
    private Drawable mIconDrawable;
    private int mIconInsetLeft, mIconInsetTop, mIconInsetRight, mIconInsetBottom;

    public ChatTabView(Context context, Drawable backgroundDrawable, Drawable iconDrawable) {
        super(context);
        mCircleDrawable = backgroundDrawable;
        mIconDrawable = iconDrawable;
        init();
    }

    private void init() {
        int insetsDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getContext().getResources().getDisplayMetrics());
        mIconInsetLeft = mIconInsetTop = mIconInsetRight = mIconInsetBottom = insetsDp;
    }

    public void setTabBackgroundColor(@ColorInt int backgroundColor) {
        mBackgroundColor = backgroundColor;
        mCircleDrawable.setColorFilter(mBackgroundColor, PorterDuff.Mode.SRC_ATOP);
    }

    public void setTabForegroundColor(@ColorInt Integer foregroundColor) {
        mForegroundColor = foregroundColor;
        if (null != mForegroundColor) {
            mIconDrawable.setColorFilter(mForegroundColor, PorterDuff.Mode.SRC_ATOP);
        } else {
            mIconDrawable.setColorFilter(null);
        }
    }

    public void setIcon(@Nullable Drawable icon) {
        mIconDrawable = icon;
        if (null != mForegroundColor && null != mIconDrawable) {
            mIconDrawable.setColorFilter(mForegroundColor, PorterDuff.Mode.SRC_ATOP);
        }
        updateIconBounds();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int previousWidth, int previousHeight) {
        super.onSizeChanged(width, height, previousWidth, previousHeight);

        // Make circle as large as View minus padding.
        mCircleDrawable.setBounds(getPaddingLeft(), getPaddingTop(), width, height);

        // Re-size the icon as necessary.
        updateIconBounds();
        invalidate();
    }

    private void updateIconBounds() {
        if (null != mIconDrawable) {
            Rect bounds = new Rect(mCircleDrawable.getBounds());
            bounds.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
            mIconDrawable.setBounds(bounds);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCircleDrawable.draw(canvas);
        if (null != mIconDrawable) {
            mIconDrawable.draw(canvas);
        }
    }
}
