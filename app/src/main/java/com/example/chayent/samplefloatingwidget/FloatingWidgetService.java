package com.example.chayent.samplefloatingwidget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    CounterFab mCounterFab;
    boolean mActivityBackground;
    private ImageView mImageViewClose;
    private FrameLayout mLinearLayoutChatBox;
    private LinearLayout mFloatingLayout;
    private Button mButtonCloseStream;
    private int mClickThreshold = 5;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        FloatingWidgetService getService() {
            // Return this instance of LocalService so clients can call public methods
            return FloatingWidgetService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setTheme(R.style.AppTheme);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null) {
            mWindowManager.removeView(mOverlayView);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            mActivityBackground = intent.getBooleanExtra("mActivityBackground", false);
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
            params.y = 200;

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Objects.requireNonNull(mWindowManager).addView(mOverlayView, params);

            Display display = mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);

            mCounterFab = mOverlayView.findViewById(R.id.floating_head);
            mCounterFab.setCount(1);

            mImageViewClose = mOverlayView.findViewById(R.id.close_chat_button);
            mImageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();
                }
            });

            mFloatingLayout = mOverlayView.findViewById(R.id.floating_layout);
            mLinearLayoutChatBox = mOverlayView.findViewById(R.id.chat_box);
//            mButtonCloseStream = mOverlayView.findViewById(R.id.stream_close_button);

            final LinearLayout layout = mOverlayView.findViewById(R.id.layout);
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

//            mButtonCloseStream.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    stopSelf();
//                }
//            });

//            mCounterFab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mLinearLayoutChatBox.getVisibility() == View.VISIBLE) {
//                        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                        params.gravity = Gravity.END | Gravity.TOP;
//                        mImageViewClose.setVisibility(View.VISIBLE);
//                        mLinearLayoutChatBox.setVisibility(View.GONE);
//                        mWindowManager.updateViewLayout(mOverlayView, params);
//                    } else {
//                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//                        params.gravity = Gravity.START | Gravity.TOP;
//                        mImageViewClose.setVisibility(View.GONE);
//                        mLinearLayoutChatBox.setVisibility(View.VISIBLE);
//                        mWindowManager.updateViewLayout(mOverlayView, params);
//                    }
//                }
//            });

            mCounterFab.setOnTouchListener(new View.OnTouchListener() {
                private int mInitialX;
                private int mInitialY;
                private float mInitialTouchX;
                private float mInitialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //remember the initial position.
                            mInitialX = params.x;
                            mInitialY = params.y;

                            //get the touch location
                            mInitialTouchX = event.getRawX();
                            mInitialTouchY = event.getRawY();
                            return true;

                        case MotionEvent.ACTION_UP:
                            //xDiff and yDiff contain the minor changes in position when the view is clicked.
                            float xDiff = mInitialTouchX - event.getRawX();
                            float yDiff = event.getRawY() - mInitialTouchY;

                            if ((Math.abs(xDiff) < mClickThreshold) && (Math.abs(yDiff) < mClickThreshold)) {
//                                Log.d("test", "onclick");
                                if (mLinearLayoutChatBox.getVisibility() == View.VISIBLE) {
                                    params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                    params.gravity = Gravity.END | Gravity.TOP;
                                    mImageViewClose.setVisibility(View.VISIBLE);
                                    mLinearLayoutChatBox.setVisibility(View.GONE);
                                    mWindowManager.updateViewLayout(mOverlayView, params);
                                } else {
                                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                                    params.height = WindowManager.LayoutParams.MATCH_PARENT;
                                    params.gravity = Gravity.START | Gravity.TOP;
                                    mImageViewClose.setVisibility(View.GONE);
                                    mLinearLayoutChatBox.setVisibility(View.VISIBLE);
                                    mWindowManager.updateViewLayout(mOverlayView, params);
                                }
                            }
//                            if (mActivityBackground) {
//                                if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {
//                                    Intent intent = new Intent(FloatingWidgetService.this, MainActivity.class);
//                                    intent.putExtra("badge_count", mCounterFab.getCount());
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//
//                                    //close the service and remove the fab view
//                                    stopSelf();
//                                }
//                            }

                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            int middle = mWidth / 2;
                            float nearestXWall = params.x >= middle ? mWidth : 0;
                            params.x = (int) nearestXWall;

                            mWindowManager.updateViewLayout(mOverlayView, params);
                            return true;

                        case MotionEvent.ACTION_MOVE:
                            int xDiffMove = Math.round(mInitialTouchX - event.getRawX());
                            int yDiffMove = Math.round(event.getRawY() - mInitialTouchY);

                            //Calculate the X and Y coordinates of the view.
                            params.x = mInitialX + xDiffMove;
                            params.y = mInitialY + yDiffMove;

                            //Update the layout with new X & Y coordinates
                            mWindowManager.updateViewLayout(mOverlayView, params);
                            return true;
                    }
                    return false;
                }
            });
        } else {
            if (!mActivityBackground) {
                mCounterFab.increase();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void replaceChatFragment(){
        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getApplicationContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.chat_box, new ChatPageFragment(), "Chat Page");
        fragmentTransaction.commitAllowingStateLoss();
    }
}
