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
import com.example.chayent.samplefloatingwidget.model.GiftModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * GiftAdapter.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 12/25/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class GiftAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<GiftModel> mGiftModelArrayList;

    public GiftAdapter(Context context, ArrayList<GiftModel> giftModelArrayList) {
        mContext = context;
        mGiftModelArrayList = giftModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        viewHolder = new GiftHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gift_layout, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GiftModel giftModel = mGiftModelArrayList.get(position);
        if (giftModel != null) {
            ((GiftHolder) holder).mImageViewCoin.setVisibility(View.VISIBLE);
            ((GiftHolder) holder).mTextView.setText(String.valueOf(giftModel.getCoin()));
            ((GiftHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGiftModelArrayList != null ? mGiftModelArrayList.size() : 0;
    }

    class GiftHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_gift_image)
        ImageView mImageViewGift;
        @BindView(R.id.item_gift_coin)
        ImageView mImageViewCoin;
        @BindView(R.id.item_gift_tv)
        TextView mTextView;

        GiftHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
