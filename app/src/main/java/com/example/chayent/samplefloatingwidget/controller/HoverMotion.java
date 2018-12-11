package com.example.chayent.samplefloatingwidget.controller;

import android.graphics.PointF;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

/**
 * HoverMotion.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class HoverMotion {

    private static final String TAG = "HoverMotion";

    private static final int RENDER_CYCLE_IN_MILLIS = 16; // 60 FPS.

    private View mView;
    private BrownianMotionGenerator mBrownianMotionGenerator = new BrownianMotionGenerator();
    private GrowAndShrinkGenerator mGrowAndShrinkGenerator = new GrowAndShrinkGenerator(0.05f);
    private boolean mIsRunning;
    private long mTimeOfLastUpdate;
    private int mDtInMillis;
    private Runnable mStateUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                // Calculate the time that's passed since the last update.
                mDtInMillis = (int) (SystemClock.elapsedRealtime() - mTimeOfLastUpdate);

                // Update visual state.
                updatePosition();
                updateGrowth();

                // Update time tracking.
                mTimeOfLastUpdate = SystemClock.elapsedRealtime();

                // Schedule next loop.
                mView.postDelayed(this, RENDER_CYCLE_IN_MILLIS);
            }
        }
    };

    public void start(@NonNull View view) {
        Log.d(TAG, "start()");
        mView = view;
        mIsRunning = true;
        mTimeOfLastUpdate = SystemClock.elapsedRealtime();
        mView.post(mStateUpdateRunnable);
    }

    public void stop() {
        Log.d(TAG, "stop()");
        mIsRunning = false;
    }

    private void updatePosition() {
        // Calculate a new position and move the View.
        PointF mPositionOffset = mBrownianMotionGenerator.applyMotion(mDtInMillis);
        mView.setTranslationX(mPositionOffset.x);
        mView.setTranslationY(mPositionOffset.y);
    }

    private void updateGrowth() {
        // Calculate and apply scaling.
        float scale = mGrowAndShrinkGenerator.applyGrowth(mDtInMillis);
        mView.setScaleX(scale);
        mView.setScaleY(scale);

        // Set elevation based on scale (the bigger, the higher).
        int baseElevation = 50;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mView.setElevation(baseElevation * scale);
        }
    }

    public static class BrownianMotionGenerator {

        private static final float FRICTION = 0.8f;

        private int mMaxDisplacementInPixels = 200;
        private PointF mPosition = new PointF(0, 200);
        private PointF mVelocity = new PointF(0, 0);

        PointF applyMotion(int dtInMillis) {
            float xConstraintAdditive = mPosition.x / mMaxDisplacementInPixels;
            float yConstraintAdditive = mPosition.y / mMaxDisplacementInPixels;

            // Randomly adjust velocity.
            float velocityXAdjustment = (float) (Math.random() * 1.0 - 0.5) - xConstraintAdditive;
            float velocityYAdjustment = (float) (Math.random() * 1.0 - 0.5) - yConstraintAdditive;
            mVelocity.offset(velocityXAdjustment, velocityYAdjustment);

            // Apply velocity to position.
            mPosition.offset(mVelocity.x, mVelocity.y);

            // Apply friction to velocity.
            mVelocity.set(mVelocity.x * FRICTION, mVelocity.y * FRICTION);

            return mPosition;
        }

    }

    public static class GrowAndShrinkGenerator {

        private static final int GROWTH_CYCLE_IN_MILLIS = 5000;

        private int mLastTimeInMillis;
        private float mGrowthFactor;

        GrowAndShrinkGenerator(float growthFactor) {
            mGrowthFactor = growthFactor;
        }

        float applyGrowth(int dtInMillis) {
            mLastTimeInMillis += dtInMillis;
            return 1.0f + (float) (Math.sin(Math.PI * ((float) mLastTimeInMillis / GROWTH_CYCLE_IN_MILLIS)) * mGrowthFactor);
        }

    }
}
