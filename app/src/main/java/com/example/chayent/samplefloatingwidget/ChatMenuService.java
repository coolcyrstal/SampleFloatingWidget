package com.example.chayent.samplefloatingwidget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;

import com.example.chayent.samplefloatingwidget.theme.HoverTheme;

import java.io.IOException;

import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;

/**
 * ChatMenuService.java
 * SampleFloatingWidget
 * <p>
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class ChatMenuService extends HoverMenuService {

    private static final String TAG = "DemoHoverMenuService";

    public static void showFloatingMenu(Context context) {
        context.startService(new Intent(context, ChatMenuService.class));
    }

    private ChatMenu mDemoHoverMenu;

    @Override
    public void onCreate() {
        super.onCreate();
        Bus.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        Bus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    protected Context getContextForHoverMenu() {
        return new ContextThemeWrapper(this, R.style.AppTheme);
    }

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        hoverView.setMenu(createHoverMenu());
        hoverView.collapse();
    }

    private HoverMenu createHoverMenu() {
        try {
            mDemoHoverMenu = new HoverMenuFactory().createDemoMenuFromCode(getContextForHoverMenu(), Bus.getInstance());
//            mDemoHoverMenuAdapter = new DemoHoverMenuFactory().createDemoMenuFromFile(getContextForHoverMenu());
            return mDemoHoverMenu;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        mDemoHoverMenu.setTheme(newTheme);
    }
}
