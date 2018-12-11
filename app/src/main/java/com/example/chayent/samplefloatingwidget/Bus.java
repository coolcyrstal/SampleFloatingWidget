package com.example.chayent.samplefloatingwidget;

import de.greenrobot.event.EventBus;

/**
 * Bus.java
 * SampleFloatingWidget
 *
 * Created by Chayen Tansritrang on 11/26/2018.
 * Copyright © Electronics Extreme Ltd. All rights reserved.
 */
public class Bus {

    private static EventBus sBus = new EventBus();

    public static EventBus getInstance() {
        return sBus;
    }
}
