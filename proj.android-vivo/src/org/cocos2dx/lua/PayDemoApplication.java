package org.cocos2dx.lua;

import android.app.Application;

import com.vivo.unionsdk.open.VivoUnionSDK;
import android.content.Context;

public class PayDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //SDK初始化, 请传入自己游戏的appid替换demo中的appid。
        VivoUnionSDK.initSdk(this, "e6e9640bb3d24a8587c36d36060a6b76", false);
    }
}
