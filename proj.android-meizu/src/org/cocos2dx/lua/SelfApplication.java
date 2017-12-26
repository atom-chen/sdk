package org.cocos2dx.lua;

import android.app.Application;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;

public class SelfApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MzGameCenterPlatform.init(this, GameConstants.GAME_ID, GameConstants.GAME_KEY);

    }

}
