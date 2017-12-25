package org.cocos2dx.lua;

import android.app.Application;
import android.util.Log;

import com.huawei.gameservice.sdk.GameServiceSDK;
import com.huawei.gameservice.sdk.control.GameCrashHandler;

public class GameApplication extends Application{
	@Override  
	public void onCreate() {  
		Log.d("GameApplication", "onCreate");
		super.onCreate();  
        // 设置捕获异常崩溃的回调
        // set crash handler
		GameServiceSDK.setCrashHandler(getApplicationContext(), new GameCrashHandler(){

			@Override
			public void onCrash(String stackInfo) {
                //Log.e("GameApplication", "onCrash:" + stackInfo);
			}
			
		});
	}  

}
