package com.example.chayent.samplefloatingwidget.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chayent.samplefloatingwidget.R;
import com.example.chayent.samplefloatingwidget.adapter.ChatAdapter;
import com.example.chayent.samplefloatingwidget.controller.HoverMotion;
import com.example.chayent.samplefloatingwidget.model.ChatModel;
import com.example.chayent.samplefloatingwidget.model.GiftModel;
import com.example.chayent.samplefloatingwidget.theme.HoverTheme;

import java.util.ArrayList;
import java.util.Objects;

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

    @BindView(R.id.chat_page_chat_item_layout)
    FrameLayout mFrameLayoutChat;
    @BindView(R.id.chat_page_gift_item_layout)
    FrameLayout mFrameLayoutGift;

    @BindView(R.id.chat_page_chat_edit_text)
    EditText mEditText;
    @BindView(R.id.chat_page_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.chat_page_gift_keyboard)
    GiftView mGiftView;

    @BindView(R.id.chat_page_gift)
    ImageView mGiftImageView;
    @BindView(R.id.chat_page_chat)
    ImageView mChatImageView;
    @BindView(R.id.chat_page_send)
    ImageView mImageViewSend;
    @BindView(R.id.chat_page_sticker)
    ImageView mImageViewSticker;

    private final EventBus mBus;
    private HoverMotion mHoverMotion;
    private Context mContext;
    private ArrayList<ChatModel> mChatModelArrayList = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private boolean isShowStickerKeyBoard = false;
    private boolean isShowKeyboard = false;
    private ArrayList<GiftModel> mGiftModelArrayList = new ArrayList<>();

    public ChatMainContent(@NonNull Context context, @NonNull EventBus bus) {
        super(context);
        mContext = context;
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

        mImageViewSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEditText.getText().toString().equals("")) {
                    addChatData(mEditText.getText().toString());
                    mEditText.setText("");
                }
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                    hideGiftKeyboard();
                } else {
                    isShowKeyboard = true;
                    hideGiftKeyboard();
                }
            }
        });

        mEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShowStickerKeyBoard){
                    hideGiftKeyboard();
                }
            }
        });

        mImageViewSticker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowKeyboard) {
                    hideKeyboard();
                    showGiftKeyboard();
                }else{
                    if (isShowStickerKeyBoard) {
                        hideGiftKeyboard();
                    } else {
                        hideKeyboard();
                        showGiftKeyboard();
                    }
                }
//                showAlert();
            }
        });

        mChatAdapter = new ChatAdapter(getContext(), mChatModelArrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        mLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mChatAdapter);

        loadData();
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

    private void loadData() {
//        GiftModel giftModel = new GiftModel();
//        giftModel.setCoin(4);
//        for (int i = 0; i < 10; i++) {
//            mGiftModelArrayList.add(giftModel);
//        }
//        mGiftView.setGiftModelArrayList(mGiftModelArrayList);
    }

    private void addChatData(String chatData) {
        ChatModel chatModel = new ChatModel();
        chatModel.setChatText(chatData);
        chatModel.setUsername("test");
        mChatModelArrayList.add(chatModel);
        mChatAdapter.notifyItemChanged(mChatModelArrayList.size());
    }

    private void showGiftKeyboard() {
        isShowStickerKeyBoard = true;
        mGiftView.setVisibility(VISIBLE);
    }

    private void hideGiftKeyboard() {
        isShowStickerKeyBoard = false;
        mGiftView.setVisibility(GONE);
    }

    private void hideKeyboard() {
        isShowKeyboard = false;
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(mEditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.setMessage("set message");
        dialog.setTitle("set title");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }
}
