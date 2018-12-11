package com.example.chayent.samplefloatingwidget.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;

import com.example.chayent.samplefloatingwidget.R;
import com.example.chayent.samplefloatingwidget.View.ChatTabView;
import com.example.chayent.samplefloatingwidget.theme.HoverTheme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.HoverMenu;

/**
 * ChatMenu.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatMenu extends HoverMenu {

    public static final String INTRO_ID = "intro";
    public static final String SELECT_COLOR_ID = "select_color";
    public static final String APP_STATE_ID = "app_state";
    public static final String MENU_ID = "menu";
    public static final String PLACEHOLDER_ID = "placeholder";

    private final Context mContext;
    private final String mMenuId;
    private HoverTheme mTheme;
    private final List<Section> mSections = new ArrayList<>();

    ChatMenu(@NonNull Context context,
             @NonNull String menuId,
             @NonNull Map<String, Content> data,
             @NonNull HoverTheme theme) throws IOException {
        mContext = context;
        mMenuId = menuId;
        mTheme = theme;

        for (String tabId : data.keySet()) {
            mSections.add(new Section(
                    new SectionId(tabId),
                    createTabView(tabId),
                    data.get(tabId)
            ));
        }
    }

    public void setTheme(@NonNull HoverTheme theme) {
        mTheme = theme;
        // TODO: need to make theme changes work again with refactored menu
        notifyMenuChanged();
    }

    private View createTabView(String sectionId) {
        if (INTRO_ID.equals(sectionId)) {
            return createTabView(R.drawable.ic_action_name, mTheme.getAccentColor(), null);
        }
//        else if (SELECT_COLOR_ID.equals(sectionId)) {
//            return createTabView(R.drawable.ic_paintbrush, mTheme.getAccentColor(), mTheme.getBaseColor());
//        } else if (APP_STATE_ID.equals(sectionId)) {
//            return createTabView(R.drawable.ic_stack, mTheme.getAccentColor(), mTheme.getBaseColor());
//        } else if (MENU_ID.equals(sectionId)) {
//            return createTabView(R.drawable.ic_menu, mTheme.getAccentColor(), mTheme.getBaseColor());
//        } else if (PLACEHOLDER_ID.equals(sectionId)) {
//            return createTabView(R.drawable.ic_pen, mTheme.getAccentColor(), mTheme.getBaseColor());
//        }
        else {
            throw new RuntimeException("Unknown tab selected: " + sectionId);
        }
    }

    private View createTabView(@DrawableRes int tabBitmapRes, @ColorInt int backgroundColor, @ColorInt Integer iconColor) {
        Resources resources = mContext.getResources();
        int elevation = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, resources.getDisplayMetrics());

        ChatTabView view = new ChatTabView(mContext, resources.getDrawable(R.drawable.tab_background), resources.getDrawable(tabBitmapRes));
        view.setTabBackgroundColor(backgroundColor);
        view.setTabForegroundColor(iconColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(elevation);
        }
        return view;
    }

    @Override
    public String getId() {
        return mMenuId;
    }

    @Override
    public int getSectionCount() {
        return mSections.size();
    }

    @Nullable
    @Override
    public Section getSection(int index) {
        return mSections.get(index);
    }

    @Nullable
    @Override
    public Section getSection(@NonNull SectionId sectionId) {
        for (Section section : mSections) {
            if (section.getId().equals(sectionId)) {
                return section;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public List<Section> getSections() {
        return new ArrayList<>(mSections);
    }
}
