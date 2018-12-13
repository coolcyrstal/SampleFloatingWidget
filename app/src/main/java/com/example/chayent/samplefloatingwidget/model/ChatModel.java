package com.example.chayent.samplefloatingwidget.model;

/**
 * ChatModel.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 12/13/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatModel {

    private String mUsername;
    private String mChatText;
    private String mUserAvatar;
    private String mChatTime;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getChatText() {
        return mChatText;
    }

    public void setChatText(String chatText) {
        mChatText = chatText;
    }

    public String getUserAvatar() {
        return mUserAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        mUserAvatar = userAvatar;
    }

    public String getChatTime() {
        return mChatTime;
    }

    public void setChatTime(String chatTime) {
        mChatTime = chatTime;
    }
}
