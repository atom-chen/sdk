package org.cocos2dx.lua;


import android.app.Application;

import com.gionee.gamesdk.floatwindow.GamePlatform;

public class   SdkApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GamePlatform.init(this, Constants.API_KEY);
    }
}

