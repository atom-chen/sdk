package org.cocos2dx.lua;

import com.qihoo.gamecenter.sdk.matrix.Matrix;

import android.app.Application;


public class SdkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 此处必须先初始化360SDK
        Matrix.initInApplication(this);
    }
}
