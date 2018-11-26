package com.example.chayent.samplefloatingwidget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.chayent.samplefloatingwidget.View.HoverIntroductionContent;
import com.example.chayent.samplefloatingwidget.theme.HoverThemeManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.content.menus.DoNothingMenuAction;
import io.mattcarroll.hover.content.menus.Menu;
import io.mattcarroll.hover.content.menus.MenuItem;
import io.mattcarroll.hover.content.menus.MenuListContent;
import io.mattcarroll.hover.content.menus.ShowSubmenuMenuAction;
import io.mattcarroll.hover.content.toolbar.ToolbarNavigator;

/**
 * HoverMenuFactory.java
 * SampleFloatingWidget
 * <p>
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class HoverMenuFactory {

    /**
     * Example of how to create a menu in code.
     * @return HoverMenu
     */
    public ChatMenu createDemoMenuFromCode(@NonNull Context context, @NonNull EventBus bus) throws IOException {
        Menu drillDownMenuLevelTwo = new Menu("Demo Menu - Level 2", Arrays.asList(
                new MenuItem(UUID.randomUUID().toString(), "Google", new DoNothingMenuAction()),
                new MenuItem(UUID.randomUUID().toString(), "Amazon", new DoNothingMenuAction())
        ));
        ShowSubmenuMenuAction showLevelTwoMenuAction = new ShowSubmenuMenuAction(drillDownMenuLevelTwo);

        Menu drillDownMenu = new Menu("Demo Menu", Arrays.asList(
                new MenuItem(UUID.randomUUID().toString(), "GPS", new DoNothingMenuAction()),
                new MenuItem(UUID.randomUUID().toString(), "Cell Tower Triangulation", new DoNothingMenuAction()),
                new MenuItem(UUID.randomUUID().toString(), "Location Services", showLevelTwoMenuAction)
        ));

        MenuListContent drillDownMenuNavigatorContent = new MenuListContent(context, drillDownMenu);

        ToolbarNavigator toolbarNavigator = new ToolbarNavigator(context);
        toolbarNavigator.pushContent(drillDownMenuNavigatorContent);

        Map<String, Content> demoMenu = new LinkedHashMap<>();
        demoMenu.put(ChatMenu.INTRO_ID, new HoverIntroductionContent(context, Bus.getInstance()));
//        demoMenu.put(ChatMenu.SELECT_COLOR_ID, new ColorSelectionContent(context, Bus.getInstance(), HoverThemeManager.getInstance(), HoverThemeManager.getInstance().getTheme()));
//        demoMenu.put(ChatMenu.APP_STATE_ID, new AppStateContent(context));
//        demoMenu.put(ChatMenu.MENU_ID, toolbarNavigator);
//        demoMenu.put(ChatMenu.PLACEHOLDER_ID, new PlaceholderContent(context, bus));

        return new ChatMenu(context, "kitchensink", demoMenu, HoverThemeManager.getInstance().getTheme());
    }
}
