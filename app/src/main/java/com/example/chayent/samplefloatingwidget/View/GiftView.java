package com.example.chayent.samplefloatingwidget.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.chayent.samplefloatingwidget.R;
import com.example.chayent.samplefloatingwidget.adapter.GiftAdapter;
import com.example.chayent.samplefloatingwidget.model.GiftModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * GiftView.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 12/25/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class GiftView extends LinearLayout {

    @BindView(R.id.gift_layout_recycler_view)
    RecyclerView mRecyclerViewGift;

    private Context mContext;
    private GiftAdapter mGiftAdapter;
    private ArrayList<GiftModel> mGiftModelArrayList = new ArrayList<>();


    public GiftView(Context context) {
        super(context);
        mContext = context;
        setUpView();
    }

    public GiftView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setUpView();
    }

    public GiftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setUpView();
    }

    public GiftView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        setUpView();
    }

    private void setUpView() {
        LayoutInflater.from(getContext()).inflate(R.layout.gift_layout, this, true);
        ButterKnife.bind(this);

        mGiftAdapter = new GiftAdapter(getContext(), mGiftModelArrayList);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
        mRecyclerViewGift.setLayoutManager(mLayoutManager);
        mRecyclerViewGift.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewGift.setAdapter(mGiftAdapter);

        GiftModel giftModel = new GiftModel();
        giftModel.setCoin(4);
        for (int i = 0; i < 10; i++) {
            mGiftModelArrayList.add(giftModel);
        }
        setGiftModelArrayList(mGiftModelArrayList);
    }

    public void setGiftModelArrayList(ArrayList<GiftModel> giftModelArrayList){
        mGiftModelArrayList = giftModelArrayList;
        mGiftAdapter.notifyDataSetChanged();
    }
}
