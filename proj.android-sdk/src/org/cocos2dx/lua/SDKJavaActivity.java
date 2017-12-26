/****************************************************************************
    类名：SDKJavaActivity 
    作用：《你来嘛英雄》游戏平台SDK Android接入类
    说明：1.所有Protected 和 Public修饰的成员变量和成员方法 不可以删除 必须复写 
         Private修饰的成员函数可以删除
         2.没有相应SDK方法的 对应方法可以为空实现
    时间：2017-01-20
    码农：zjd
    平台：360
****************************************************************************/
package org.cocos2dx.lua;
import org.cocos2dx.lib.Cocos2dxLuaJavaBridge;
import org.cocos2dx.lib.Cocos2dxActivity;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;
import android.util.Log;
import android.app.Activity;
import android.text.TextUtils;
import android.content.ClipboardManager;
import android.content.Context;

//导入sdk包

public class SDKJavaActivity extends Cocos2dxActivity{

  
  /*
   platname       platform      usertype
   体验服（官网）     1             1
   体验服（小米）     1             2
   体验服（360）     1             3
   体验服（UC）      1             4
   小米服            2             0
   360服            3             0
   UC服             4             0
   联想             9             0
  */
  public static SDKJavaActivity sdkJavaActivity = null;
  private static final int PLATFROM = 5;
  private static final int USERTYPE = 22;
  protected static final boolean isDebug = true;//是否是测试模式

  //全局变量

  //得到sdkJavaActivity
  public static SDKJavaActivity getSDKJavaActivity(){
    return sdkJavaActivity;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      sdkJavaActivity = this;
      AppActivity.sdkToastLog("开始初始化=======");
  }

  //初始化回调
  public static void sdkJavaInit(){
    sdkJavaLogin();
  }

  //登录游戏 必须调用
  public static void sdkJavaLogin() {
    AppActivity.sdkToastLog("开始登录=======");
    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
          //此时已在主线程中，可以更新UI了
          //调用登录接口

       }
    });
  }

  //提交游戏角色信息
  public static void sdkJavaSubmitData(final String submitDatas) {
      AppActivity.sdkToastLog("提交游戏角色信息=======");
      SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
            //此时已在主线程中，可以更新UI了
            //调用提交游戏角色信息
            String[] submitData = submitDatas.split("\\$");

         }
      });
  }

  //切换账号
  public static void sdkJavaLogout() {
       AppActivity.sdkToastLog("调用切换账号=======");
       sdkJavaLogin();
  }

  //退出游戏 必须调用
  public static void sdkJavaExit() {
      AppActivity.sdkToastLog("退出游戏=======");
      System.exit(0);
  }
  
  //支付 必须调用
  public static void sdkJavaPay(final String payDatas) {
    SDKJavaActivity.getSDKJavaActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
          //此时已在主线程中，可以更新UI了
          String[] payData = payDatas.split("\\$");
          String  userId=payData[0];
          String payid = payData[1];
          String costMoney=payData[2];
          String payUrl=payData[3];
          String ip = payData[4];
          String port = payData[5];
          String produceID = payData[7];
          String produceName = payData[8];
          String serverIndex = payData[9];
       }
    });
  }
}
