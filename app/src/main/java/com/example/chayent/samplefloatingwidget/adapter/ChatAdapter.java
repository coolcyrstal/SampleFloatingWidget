package com.example.chayent.samplefloatingwidget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chayent.samplefloatingwidget.R;
import com.example.chayent.samplefloatingwidget.model.ChatModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ChatAdapter.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 12/13/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ChatModel> mChatModelArrayList;

    public ChatAdapter(Context context, ArrayList<ChatModel> chatModelArrayList) {
        mContext = context;
        mChatModelArrayList = chatModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_view, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel chatModel = mChatModelArrayList.get(position);
        if(chatModel != null){
            ((ChatViewHolder)holder).mTextViewUsername.setText(chatModel.getUsername().concat(mContext.getString(R.string.chat_text_user_indent)));
            ((ChatViewHolder)holder).mTextViewChat.setText(chatModel.getChatText());
        }
    }

    @Override
    public int getItemCount() {
        return mChatModelArrayList != null ? mChatModelArrayList.size() : 0;
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_chat_user_image)
        ImageView mImageView;
        @BindView(R.id.item_chat_user_name)
        TextView mTextViewUsername;
        @BindView(R.id.item_chat_tv)
        TextView mTextViewChat;
        @BindView(R.id.item_chat_time_tv)
        TextView mTextViewTime;


        ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
