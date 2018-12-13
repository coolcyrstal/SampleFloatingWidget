package com.example.chayent.samplefloatingwidget.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chayent.samplefloatingwidget.R;
import com.example.chayent.samplefloatingwidget.controller.HoverMotion;
import com.example.chayent.samplefloatingwidget.theme.HoverTheme;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.mattcarroll.hover.Content;

/**
 * HoverIntroductionContent.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatMainContent extends FrameLayout implements Content {

    @BindView(R.id.chat_page_gift)
    ImageView mGiftImageView;
    @BindView(R.id.chat_page_chat)
    ImageView mChatImageView;
    @BindView(R.id.chat_page_chat_item_layout)
    FrameLayout mFrameLayoutChat;
    @BindView(R.id.chat_page_gift_item_layout)
    FrameLayout mFrameLayoutGift;

    private final EventBus mBus;
    private HoverMotion mHoverMotion;

    public ChatMainContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mBus = bus;
        setUpView();
    }

    private void setUpView() {
        LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat_page, this, true);
        ButterKnife.bind(this);

        mHoverMotion = new HoverMotion();

        mGiftImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "on gift click", Toast.LENGTH_SHORT).show();
                mFrameLayoutGift.setBackground(getResources().getDrawable(R.drawable.shape_circle));
                mFrameLayoutChat.setBackground(null);
                mGiftImageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
                mChatImageView.setColorFilter(getResources().getColor(R.color.colorLiveTextTitle));
            }
        });

        mChatImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mFrameLayoutChat.setBackground(getResources().getDrawable(R.drawable.shape_circle));
                mFrameLayoutGift.setBackground(null);
                mGiftImageView.setColorFilter(getResources().getColor(R.color.colorLiveTextTitle));
                mChatImageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
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
