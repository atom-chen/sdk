package org.cocos2dx.lua;

import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.common.util.AppUtil;
import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
        String appSecret = "3a66577922600091ffc3aBaa40eBc573";
		GameCenterSDK.init(appSecret, this);
	}
}
