/****************************************************************************
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2011      Zynga Inc.
Copyright (c) 2013-2014 Chukong Technologies Inc.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.lua;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.ArrayList;
import android.widget.Toast;

import org.cocos2dx.lib.Cocos2dxActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.WindowManager;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.KeyEvent;
import android.telephony.TelephonyManager;
import android.view.View;

public class AppActivity extends SDKJavaActivity{

    static String hostIPAdress = "0.0.0.0";
    public static ClipboardManager cm;

    static String phoneName="";
    static String imei="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(nativeIsLandScape()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        
        // Check the wifi is opened when the native is debug.
        if(nativeIsDebug())
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            if(!isNetworkConnected())
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Warning");
                builder.setMessage("Please open WIFI for debuging...");
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        finish();
                        System.exit(0);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                builder.setCancelable(true);
                builder.show();
            }
            hostIPAdress = getHostIpAddress();
        }

        //++隐藏导航栏，全屏游戏
        hideNavigationBar();
        //游戏不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //剪贴板
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //获取手机型号
        phoneName = android.os.Build.MODEL ;
        TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = mTm.getDeviceId();
    }

    //++窗口焦点改变
    @Override  
    public void onWindowFocusChanged(boolean hasFocus) {  
        super.onWindowFocusChanged(hasFocus);  
        if( hasFocus ) {  
            hideNavigationBar();  
        }  
    }

    //++隐藏导航栏
    private void hideNavigationBar() {  
        // TODO Auto-generated method stub  
        final View decorView = getWindow().getDecorView();  
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE  
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  
                | View.SYSTEM_UI_FLAG_FULLSCREEN  
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;  
        decorView.setSystemUiVisibility(flags);  
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {  
            @Override  
            public void onSystemUiVisibilityChange(int visibility) {  
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {  
                    decorView.setSystemUiVisibility(flags);  
                }  
            }  
        });  
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (cm != null) {  
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
            ArrayList networkTypes = new ArrayList();
            networkTypes.add(ConnectivityManager.TYPE_WIFI);
            try {
                networkTypes.add(ConnectivityManager.class.getDeclaredField("TYPE_ETHERNET").getInt(null));
            } catch (NoSuchFieldException nsfe) {
            }
            catch (IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
            if (networkInfo != null && networkTypes.contains(networkInfo.getType())) {
                return true;  
            }  
        }  
        return false;  
    } 
     
    public String getHostIpAddress() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return ((ip & 0xFF) + "." + ((ip >>>= 8) & 0xFF) + "." + ((ip >>>= 8) & 0xFF) + "." + ((ip >>>= 8) & 0xFF));
    }
    
    public static String getLocalIpAddress() {
        return hostIPAdress;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    private static native boolean nativeIsLandScape();
    private static native boolean nativeIsDebug();


    // 气泡弹窗 SDKJavaActivity中 气泡弹出 必须调用该方法 不可以调用 Toast.makeText
    // 直接调用 Toast.makeText 方法可能发生未知错误
    public static void sdkToastLog(final String loginfo){
        if(isDebug){
            sdkJavaActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                // TODO Auto-generated method stub
                    try {
                        Toast.makeText(sdkJavaActivity, loginfo, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        
    }

    //java调用lua 登录成功的回调
    public static void sdkLoginCallBack(final String uID, final int userType, final int platForm,final String token){
        SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                  //此时已在主线程中，可以更新UI了
                  SDKJavaActivity.getSDKJavaActivity().runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        Cocos2dxLuaJavaBridge.callLuaGlobalFunctionWithString("sdkLoginCallBack", uID + "," + userType + "," + platForm+ ","+phoneName+","+imei+","+token);
                    }
                  });
             }
        });
    }

    //lua 调用登录接口
    public static int sdkInitAndLogin() {
        SDKJavaActivity.sdkJavaInit();
        return 1; 
    }

    //lua 调用登录接口
    public static int sdkSubmitData(final String submitDatas) {
        SDKJavaActivity.sdkJavaSubmitData(submitDatas);
        return 1;  
    }
    
    public static int sdkLogout() {
        SDKJavaActivity.sdkJavaLogout();
        return 1; 
    }

    public static int sdkExit() {
        SDKJavaActivity.sdkJavaExit();
        return 1;  
    }

     public static int sdkPay(final String  costMoneyAnduserId) {
        SDKJavaActivity.sdkJavaPay(costMoneyAnduserId);
        return 1;  
    }

    public static int copyText(final String  text) {
        cm.setText(text);
        return 1;  
    }

    /*
    //lua 调用微信分享到朋友圈
    public static int wxSdkShareTextToMoments(final String textContent) {
        SDKJavaActivity.wxSdkJavaShareTextToMoments(textContent);
        return 1;
    }
    //
    public static int wxSdkShareImageToMoments() {
        SDKJavaActivity.wxSdkJavaShareImageToMoments();
        return 1;
    }
    //
    public static int wxSdkShareWebToMoments() {
        SDKJavaActivity.wxSdkJavaShareWebToMoments();
        return 1;
    }
    */
}
