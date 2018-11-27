package com.example.chayent.samplefloatingwidget.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chayent.samplefloatingwidget.controller.HoverMotion;
import com.example.chayent.samplefloatingwidget.theme.HoverTheme;
import com.example.chayent.samplefloatingwidget.R;

import de.greenrobot.event.EventBus;
import io.mattcarroll.hover.Content;

/**
 * HoverIntroductionContent.java
 * SampleFloatingWidget
 * <p>
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatMainContent extends FrameLayout implements Content {

    private final EventBus mBus;
    private ImageView mLogo;
    private HoverMotion mHoverMotion;

    public ChatMainContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mBus = bus;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat_page, this, true);

        mLogo = findViewById(R.id.chat_page_gift);
        mHoverMotion = new HoverMotion();
        mLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "on gift click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBus.registerSticky(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mBus.unregister(this);
        super.onDetachedFromWindow();
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {

    }

    @Override
    public void onHidden() {

    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {

    }
}
