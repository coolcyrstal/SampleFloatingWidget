package com.example.chayent.samplefloatingwidget;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.andremion.counterfab.CounterFab;

import java.util.Objects;

/**
 * FloatingWidgetService.java
 * SampleFloatingWidget
 * Created by Chayen Tansritrang on 11/16/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class FloatingWidgetService extends Service {

    private WindowManager mWindowManager;
    private View mOverlayView;
    int mWidth;
    CounterFab counterFab;
    boolean activity_background;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setTheme(R.style.AppTheme);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false);
        }

        if (mOverlayView == null) {
            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

            final WindowManager.LayoutParams params;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
            }

            //Specify the view position
            params.gravity = Gravity.END | Gravity.TOP;        //Initially view will be added to top-left corner
            params.x = 0;
            params.y = 100;

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Objects.requireNonNull(mWindowManager).addView(mOverlayView, params);

            Display display = mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);

            counterFab = mOverlayView.findViewById(R.id.fabHead);
            counterFab.setCount(1);

            final RelativeLayout layout = mOverlayView.findViewById(R.id.layout);
            ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = layout.getMeasuredWidth();

                    //To get the accurate middle of the screen we subtract the width of the android floating widget.
                    mWidth = size.x - width;

                }
            });

            counterFab.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //remember the initial position.
                            initialX = params.x;
                            initialY = params.y;

                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:
                            if (activity_background) {
                                //xDiff and yDiff contain the minor changes in position when the view is clicked.
                                float xDiff = initialTouchX - event.getRawX();
                                float yDiff = event.getRawY() - initialTouchY;

                                if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {
                                    Intent intent = new Intent(FloatingWidgetService.this, MainActivity.class);
                                    intent.putExtra("badge_count", counterFab.getCount());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                    //close the service and remove the fab view
                                    stopSelf();
                                }
                            }

                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            int middle = mWidth / 2;
                            float nearestXWall = params.x >= middle ? mWidth : 0;
                            params.x = (int) nearestXWall;

                            mWindowManager.updateViewLayout(mOverlayView, params);
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            int xDiff = Math.round(initialTouchX - event.getRawX());
                            int yDiff = Math.round(event.getRawY() - initialTouchY);

                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff;
                            params.y = initialY + yDiff;

                            //Update the layout with new X & Y coordinates
                            mWindowManager.updateViewLayout(mOverlayView, params);
                            return true;
                    }
                    return false;
                }
            });
        } else {
            if (!activity_background) {
                counterFab.increase();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
