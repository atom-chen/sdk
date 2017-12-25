package org.cocos2dx.lua;

import android.app.Application;

import com.aiyou.sdk.LGSDK;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LGSDK.init(this, "211538", true);
    }
}
